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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.service.SimpleCommentService;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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
}
