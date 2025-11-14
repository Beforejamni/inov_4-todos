package study.todos.domain.comment.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.todos.common.dto.Api;
import study.todos.common.exception.GlobalExceptionHandler;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.dto.UpdateCommentReq;
import study.todos.domain.comment.exception.CommentErrorCode;
import study.todos.domain.comment.exception.CommentException;
import study.todos.domain.comment.service.SimpleCommentService;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private final SimpleCommentRes res = new SimpleCommentRes(1L, "comments", "userName");
    private final SimpleCommentReq req = new SimpleCommentReq("comments", "userName");

    @BeforeEach
    void init() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(om))
                //페이지처리를 위해 추가해줘야 한다.
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("Comment_저장_성공")
    void saveComment_성공() throws Exception {

        //ArgumentMatcher - eq()
        Mockito.when(commentService.save(any(),any(SimpleCommentReq.class))).thenReturn(res);

        mockMvc.perform(post("/comments/{todoId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comments").value("comments"))
                .andExpect(jsonPath("$.userName").value("userName"));
    }

    @Test
    @DisplayName("댓글_저장_실패")
    void saveComment_실패() throws Exception {
        //given
        BDDMockito.given(commentService.save(any(Long.class), any(SimpleCommentReq.class))).willThrow(new TodoException(TodoErrorCode.NOT_FOUND));

        mockMvc.perform(post("/comments/{todoId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(TodoErrorCode.NOT_FOUND.getMessage()));

        BDDMockito.verify(commentService).save(any(Long.class), any(SimpleCommentReq.class));
    }


    @Test
    @DisplayName("댓글_찾기_성공")
    void findComment_성공() throws Exception {

        Mockito.when(commentService.findComment(any(Long.class))).thenReturn(res);

        mockMvc.perform(get("/comments/comments/{commentId}" ,1L)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").value("comments"))
                .andExpect(jsonPath("$.userName").value("userName"));

        BDDMockito.verify(commentService).findComment(1L);
    }

    @Test
    @DisplayName("댓글_찾기_실패")
    void findComment_실패() throws Exception {

        BDDMockito.given(commentService.findComment(anyLong())).willThrow( new CommentException(CommentErrorCode.NOT_FOUND));

        mockMvc.perform(get("/comments/comments/{commentId}", 1L)
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(CommentErrorCode.NOT_FOUND.getMessage()));

        BDDMockito.verify(commentService).findComment(1L);
    }

    @Test
    @DisplayName("게시글_댓글_전체_조회")
    void findComments_성공() throws Exception {
        //give
        Api<List<SimpleCommentRes>> serviceRes = SimpleCommentControllerUnitTest.extractApiComments();
        BDDMockito.given(commentService.findComments(anyLong(), any(Pageable.class))).willReturn(serviceRes);

        mockMvc.perform(get("/comments/{todoId}",1L)
                        .param("page", "0")
                        .param("size", "10")
                //param을 이용해서 보낼 때, Application_Json 타입으로
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentElement").value(10))
                .andExpect(jsonPath("$.pagination.totalPage").value(2))
                .andExpect(jsonPath("$.pagination.totalElement").value(12));

        Pageable pageable = PageRequest.of(0, 10);
        BDDMockito.verify(commentService).findComments(1L, pageable);
    }

    @Test
    @DisplayName("댓글_수정_성공")
    void updateComment_성공() throws Exception {
        UpdateCommentReq updateCommentReq = new UpdateCommentReq("comments");
        BDDMockito.given(commentService.updateComment(anyLong(),any(UpdateCommentReq.class))).willReturn(res);

        mockMvc.perform(post("/comments/update/{commentId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(updateCommentReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").value("comments"))
                .andExpect(jsonPath("$.userName").value("userName"));

        BDDMockito.verify(commentService).updateComment(1L, updateCommentReq);
    }

    @Test
    @DisplayName("댓글_수정_실패")
    void updateComment_실패() throws Exception {
        UpdateCommentReq updateCommentReq = new UpdateCommentReq("comments");
        BDDMockito.given(commentService.updateComment(anyLong(), any(UpdateCommentReq.class))).willThrow(new CommentException(CommentErrorCode.NOT_FOUND));

        mockMvc.perform(post("/comments/update/{commentId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(updateCommentReq)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(CommentErrorCode.NOT_FOUND.getMessage()));

        BDDMockito.verify(commentService).updateComment(1L, updateCommentReq);
    }

    @Test
    @DisplayName("댓글_삭제_성공")
    void deleteComment_성공() throws Exception {
        Map<String, String> message = Map.of("message", "댓글이 삭제되었습니다.");
        BDDMockito.given(commentService.deleteComment(anyLong())).willReturn(message);

        mockMvc.perform(post("/comments/delete/{commentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.get("message")));

        BDDMockito.verify(commentService).deleteComment(1L);
    }

}
