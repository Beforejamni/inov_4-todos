package study.todos.domain.member.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.dto.Api;
import study.todos.common.dto.Pagination;
import study.todos.common.exception.GlobalExceptionHandler;
import study.todos.domain.Member.controller.SimpleMemberController;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.dto.UpdateMemberReq;
import study.todos.domain.Member.exception.MemberErrorCode;
import study.todos.domain.Member.exception.MemberException;
import study.todos.domain.Member.service.SimpleMemberService;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SimpleMemberControllerTest {

    @Mock
    private SimpleMemberService simpleMemberService;

    @InjectMocks
    private SimpleMemberController simpleMemberController;

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ObjectMapper om;

    private final SimpleMemberRes res =  new SimpleMemberRes("userName", "email");

    @BeforeEach
    void init() {
        om = new ObjectMapper().registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders
                .standaloneSetup(simpleMemberController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(om))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("유저_저장")
    void saveMember() throws Exception {
        SimpleMemberReq req = new SimpleMemberReq("userName", "email");
        BDDMockito.given(simpleMemberService.saveMember(any(SimpleMemberReq.class))).willReturn(res);

        mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberName").value("userName"))
                .andExpect(jsonPath("$.email").value("email"));

        BDDMockito.verify(simpleMemberService).saveMember(req);
    }

    @Test
    @DisplayName("유저_조회_성공")
    void findMemberById_성공() throws Exception {
        BDDMockito.given(simpleMemberService.findMember(anyLong())).willReturn(res);

        mockMvc.perform(get("/users/{memberId}", 1L)
                .content(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberName").value("userName"))
                .andExpect(jsonPath("$.email").value("email"));

        BDDMockito.verify(simpleMemberService).findMember(1L);
    }

    @Test
    @DisplayName("유저_조회_실패")
    void findMemberById_실패() throws Exception {
        BDDMockito.given(simpleMemberService.findMember(anyLong())).willThrow(new MemberException(MemberErrorCode.NOT_FOUND));

        mockMvc.perform(get("/users/{memberId}", 1L)
                .content(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(MemberErrorCode.NOT_FOUND.getMessage()));

        BDDMockito.verify(simpleMemberService).findMember(1L);
    }

    @Test
    @DisplayName("유저_전체_조회")
    void findMembers() throws Exception {
        List<SimpleMemberRes> response = IntStream.range(1, 20).mapToObj(i -> new SimpleMemberRes("userName" + i, "email")).toList();
        BDDMockito.given(simpleMemberService.findMembers()).willReturn(response);

         mockMvc.perform(get("/users")
                        .contentType(MediaType.TEXT_PLAIN))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.[0].memberName").value("userName1"))
                 .andExpect(jsonPath("$.[1].memberName").value("userName2"));

         BDDMockito.verify(simpleMemberService).findMembers();
    }


    @Test
    @DisplayName("일정_유저_전체_조회")
    void findMembersByTodo() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<SimpleMemberRes> list = IntStream.range(1, 20).mapToObj(i -> new SimpleMemberRes("userName" + i, "email")).toList();
        Pagination pagination = new Pagination(page, size, 10, 2, Long.valueOf(list.size()));
        List<SimpleMemberRes> subList = list.subList(0, 10);
        Api<List<SimpleMemberRes>> response = new Api<>(subList, pagination);

        BDDMockito.given(simpleMemberService.findMembersByTodoId(anyLong(),any(Pageable.class))).willReturn(response);

        mockMvc.perform(get("/users/todo/{todoId}", 1L)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                                .param("page", "0")
                                .param("size", "10")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentElement").value(10))
                .andExpect(jsonPath("$.pagination.totalElement").value(19));

        BDDMockito.verify(simpleMemberService).findMembersByTodoId(1L,pageable);
    }

    @Test
    @DisplayName("유저_수정_성공")
    void updateMember() throws Exception {
        UpdateMemberReq updateMemberReq = new UpdateMemberReq("updateMember", "updateEmail");
        SimpleMemberRes simpleMemberRes = new SimpleMemberRes("updateMember", "updateEmail");

        BDDMockito.given(simpleMemberService.updateMember(anyLong(),any(UpdateMemberReq.class))).willReturn(simpleMemberRes);

        mockMvc.perform(post("/users/update/{memberId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updateMemberReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberName").value("updateMember"))
                .andExpect(jsonPath("$.email").value("updateEmail"));

        BDDMockito.verify(simpleMemberService).updateMember(1L, updateMemberReq);
    }

    @Test
    @DisplayName("유저_수정_실패")
    void updateMember_실패() throws Exception {
        UpdateMemberReq updateMemberReq = new UpdateMemberReq("updateMember", "updateEmail");
        BDDMockito.given(simpleMemberService.updateMember(anyLong(),any(UpdateMemberReq.class))).willThrow(new MemberException(MemberErrorCode.NOT_FOUND));

        mockMvc.perform(post("/users/update/{memberId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(updateMemberReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(MemberErrorCode.NOT_FOUND.getMessage()));

        BDDMockito.verify(simpleMemberService).updateMember(1L,updateMemberReq);
    }

    @Test
    @DisplayName("유저_삭제_성공")
    void deleteMember() throws Exception {
        Map<String, String> message = Map.of("message", "유저가 삭제되었습니다.");

        BDDMockito.given(simpleMemberService.deleteMember(anyLong())).willReturn(message);

        mockMvc.perform(delete("/users/delete/{memberId}", 1L)
                    .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.get("message")));

        BDDMockito.verify(simpleMemberService).deleteMember(1L);
    }

    @Test
    @DisplayName("유저_삭제_실패")
    void deleteMember_실패() throws Exception {
        UpdateMemberReq updateMemberReq = new UpdateMemberReq("updateMember", "updateEmail");
        BDDMockito.given(simpleMemberService.deleteMember(anyLong())).willThrow(new MemberException(MemberErrorCode.NOT_FOUND));

        mockMvc.perform(delete("/users/delete/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(updateMemberReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(MemberErrorCode.NOT_FOUND.getMessage()));

        BDDMockito.verify(simpleMemberService).deleteMember(1L);
    }
}
