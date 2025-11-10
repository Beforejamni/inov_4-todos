package study.todos.domain.comment.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.todos.common.exception.GlobalExceptionHandler;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.service.CommentService;
import study.todos.domain.service.SimpleCommentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SimpleCommentControllerTest {

    @Mock
    private SimpleCommentService commentService;

    @InjectMocks
    private SimpleCommentController commentController;

    private MockMvc mockMvc;

    private final ObjectMapper om = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(om))
                .build();
    }

    @Test
    @DisplayName("Comment_저장_성공")
    void saveComment() throws Exception {

        SimpleCommentReq req = new SimpleCommentReq("comments", "userName");

        //any()는 when/verify 등 컨텍스트 안에서만 사용
        SimpleCommentRes res = new SimpleCommentRes(1L, "comments", "userName");

        //ArgumentMatcher - eq()
        Mockito.when(commentService.save(any(),any(SimpleCommentReq.class))).thenReturn(res);

        mockMvc.perform(post("/comments/{todoId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comments").value("comments"))
                .andExpect(jsonPath("$.userName").value("userName"));
    }

}
