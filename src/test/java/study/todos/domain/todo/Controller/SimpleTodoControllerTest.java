package study.todos.domain.todo.Controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.todos.common.exception.GlobalExceptionHandler;
import study.todos.domain.todo.controller.SimpleTodoController;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.service.TodoService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SimpleTodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private SimpleTodoController todoController;

    //HTTP 요청 작성 및 컨트롤러의 응답 검증
    private MockMvc mockMvc;


    //자바 객체와 JSON을 서로 변환하는 Jackson 라이브러리
     private final ObjectMapper om = new ObjectMapper()
            .registerModule(new JavaTimeModule()) //java.time을 직렬화/역직렬화
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //비활성화


    @BeforeEach
    void init() {

        //Spring 통합이랑 같이 쓰면 안됨 -> 인스턴스가 섞여 목/메시지컨버터/직렬화 설정이 엇갈릴 수 있음
        mockMvc = MockMvcBuilders
                //특정 컨트롤러를 MockMvc에 설정
                .standaloneSetup(todoController)
                //GlobalHandler 등록
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()
                        .registerModule(new JavaTimeModule())))
                .build();
    }


    @Test
    @DisplayName("Todo_저장_성공")
    void saveTodo() throws Exception {

        SimpleTodoReq req = new SimpleTodoReq("jamni", "title", "contents");


        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now(fixed);

        SimpleTodoRes res = new SimpleTodoRes("jamni", "title", "contents", now, now);

        Mockito.when(todoService.saveTodo(any(SimpleTodoReq.class))).thenReturn(res);

        MvcResult ret = mockMvc
                //HTTP 요청을 실행
                .perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))

                //컨트롤러의 응답을 검증
                .andExpect(status().isCreated())

                //Json 응답에서 속성 값을 확인
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andReturn();

    }

    @Test
    @DisplayName("Todo_조회_성공")
    void findTodo_성공() throws Exception {

        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now(fixed);

        SimpleTodoRes res = new SimpleTodoRes("jamni", "title", "contents", now, now);

        Mockito.when(todoService.findTodo(any(Long.class))).thenReturn(res);

        mockMvc.perform(get("/todos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value("jamni"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.contents").value("contents"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andReturn();
    }

    @Test
    @DisplayName("Todo_조회_실패")
    void findTodo_실패() throws Exception {

        BDDMockito.given(todoService.findTodo(any(Long.class))).willThrow(new TodoException(TodoErrorCode.NOT_FOUND));

        mockMvc.perform(
                get("/todos/{todoId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("일정을 찾을 수 없습니다."));

        //예상대로 호출되었는지 검증
        verify(todoService).findTodo(any(Long.class));
        //어떠한 상호작용도 없음을 검증
        verifyNoMoreInteractions(todoService);
    }
}
