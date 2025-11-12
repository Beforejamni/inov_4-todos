package study.todos.domain.comment.controller;


import org.assertj.core.api.Assertions;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.dto.Api;
import study.todos.common.dto.Pagination;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.exception.CommentErrorCode;
import study.todos.domain.comment.exception.CommentException;
import study.todos.domain.comment.service.SimpleCommentService;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SimpleCommentControllerUnitTest {

    @Mock
    SimpleCommentService commentService;

    @InjectMocks
    SimpleCommentController commentController;

    private SimpleCommentReq req;
    private SimpleCommentRes res;

    @BeforeEach
    void init() {
         req = new SimpleCommentReq("comments", "userName");
         res = new SimpleCommentRes(1L, "comments", "userName");
    }


    @Test
    @DisplayName("댓글_저장_성공")
    void save_성공() {
        BDDMockito.given(commentService.save(any(Long.class), any(SimpleCommentReq.class))).willReturn(res);

        ResponseEntity<SimpleCommentRes> save = commentController.save(1L, req);

        Assertions.assertThat(save.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        SimpleCommentRes body = save.getBody();
        Assertions.assertThat(body.getComments()).isEqualTo("comments");
        Assertions.assertThat(body.getUserName()).isEqualTo("userName");

        BDDMockito.verify(commentService).save(any(), any());
    }

    @Test
    @DisplayName("댓글_저장_실패")
    void save_실패() {
        BDDMockito.given(commentService.save(any(Long.class), any(SimpleCommentReq.class))).willThrow(new TodoException(TodoErrorCode.NOT_FOUND));

        TodoException todoException = assertThrows(TodoException.class,
                () -> commentController.save(1L, req));

        Assertions.assertThat(todoException.getStatus()).isEqualTo(TodoErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(todoException.getMessage()).isEqualTo(TodoErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글_찾기_성공")
    void findComment_성공() {
        BDDMockito.given(commentService.findComment(any(Long.class))).willReturn(res);

        ResponseEntity<SimpleCommentRes> comment = commentController.findComment(1L);

        Assertions.assertThat(comment.getStatusCode()).isEqualTo(HttpStatus.OK);

        SimpleCommentRes body = comment.getBody();
        Assertions.assertThat(body.getComments()).isEqualTo("comments");
        Assertions.assertThat(body.getUserName()).isEqualTo("userName");
    }

    @Test
    @DisplayName("댓글_찾기_실패")
    void findComment_실패() {
        BDDMockito.given(commentService.findComment(anyLong())).willThrow(new CommentException(CommentErrorCode.NOT_FOUND));

        CommentException commentException = assertThrows(CommentException.class,
                () -> commentController.findComment(1L));

        Assertions.assertThat(commentException.getStatus()).isEqualTo(CommentErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(commentException.getMessage()).isEqualTo(CommentErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시글_모든_댓글_조회")
    void findComments_성공(){
        Pageable  pageable = PageRequest.of(0, 10);

        Api<List<SimpleCommentRes>> serviceRes = extractApiComments();

        BDDMockito.given(commentService.findComments(anyLong(), any(Pageable.class))).willReturn(serviceRes);

        ResponseEntity<Api<List<SimpleCommentRes>>> response = commentController.findComments(1L, pageable);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(serviceRes);

        BDDMockito.verify(commentService).findComments(1L, pageable);
    }

    public static Api<List<SimpleCommentRes>> extractApiComments() {
        List<SimpleCommentRes> simpleCommentResList = IntStream.range(1, 13)
                .mapToObj(i -> new SimpleCommentRes(Long.valueOf(i), "comments" + i, "userName"))
                .toList();

        Pagination pagination = new Pagination(0, 10, 10, 2, 12L);

        return new Api<>(simpleCommentResList, pagination);
    }
}
