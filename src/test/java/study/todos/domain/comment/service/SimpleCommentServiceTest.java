package study.todos.domain.comment.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.comment.exception.CommentErrorCode;
import study.todos.domain.comment.exception.CommentException;
import study.todos.domain.comment.repository.JpaCommentRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SimpleCommentServiceTest {


    @Mock
    private JpaTodoRepository jpaTodoRepository;

    @Mock
    private JpaCommentRepository jpaCommentRepository;

    @InjectMocks
    private SimpleCommentService simpleCommentService;


    @Test
    @DisplayName("댓글_저장_성공")
    void save_성공() {

        //given
        Todo todo = new Todo();
        SimpleCommentReq simpleCommentReq = new SimpleCommentReq("comments", "userName");

        BDDMockito.when(jpaTodoRepository.findById(any(Long.class))).thenReturn(Optional.of(todo));
        BDDMockito.given(jpaCommentRepository.save(any(Comment.class))).willReturn(new Comment(todo, "comments", "userName"));
        //when
        SimpleCommentRes save = simpleCommentService.save(1L, simpleCommentReq);

        //then
        Assertions.assertThat(save.getComments()).isEqualTo("comments");
        Assertions.assertThat(save.getUserName()).isEqualTo("userName");

        BDDMockito.verify(jpaTodoRepository).findById(any(Long.class));
        BDDMockito.verify(jpaCommentRepository).save(any(Comment.class));

    }

    @Test
    @DisplayName("댓글_저장_실패_일정_부재")
    void save_실패() {

        //given
        SimpleCommentReq simpleCommentReq = new SimpleCommentReq("comments", "userName");
        BDDMockito.given(jpaTodoRepository.findById(any(Long.class))).willThrow(new TodoException(TodoErrorCode.NOT_FOUND));

        //when
        TodoException todoException = assertThrows(TodoException.class,
                () -> simpleCommentService.save(1L, simpleCommentReq));

        Assertions.assertThat(todoException.getStatus()).isEqualTo(TodoErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(todoException.getMessage()).isEqualTo(TodoErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글_찾기_성공")
    void findComment_성공() {
        //given
        Todo todo = new Todo();
        Comment comment = new Comment(todo, "comments", "userName");
        BDDMockito.given(jpaCommentRepository.findById(any(Long.class))).willReturn(Optional.of(comment));

        SimpleCommentRes ret  = simpleCommentService.findComment(1L);

        Assertions.assertThat(ret.getComments()).isEqualTo("comments");
        Assertions.assertThat(ret.getUserName()).isEqualTo("userName");
    }

    @Test
    @DisplayName("댓글_찾기_실패")
    void findComment_실패() {

        BDDMockito.given(jpaCommentRepository.findById(any(Long.class))).willReturn(Optional.empty());


        CommentException commentException = assertThrows(CommentException.class,
                () -> simpleCommentService.findComment(1L));

        Assertions.assertThat(commentException.getStatus()).isEqualTo(CommentErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(commentException.getMessage()).isEqualTo(CommentErrorCode.NOT_FOUND.getMessage());
    }
}
