package study.todos.domain.comment.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.dto.Api;
import study.todos.common.dto.Pagination;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.dto.UpdateCommentReq;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.comment.exception.CommentErrorCode;
import study.todos.domain.comment.exception.CommentException;
import study.todos.domain.comment.repository.JpaCommentRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

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

    @Test
    @DisplayName("게시물_전체_댓글_조회")
    void findComments_성공() {
        //given
        Todo todo = new Todo();
        todo.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Pagination pagination = new Pagination(0 , 10, 10, 2 ,12L);

        List<Comment> comments = IntStream.range(1, 13).mapToObj(i -> new Comment(Long.valueOf(i), todo, "comment" + i, "userName")).toList();

        //pageSize만큼 잘라서 보내줘야 한다.
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize() , comments.size());
        List<Comment> commentsSub = comments.subList(start, end);

        Page<Comment> commentsPage = new PageImpl<>(commentsSub, pageable, comments.size());

        BDDMockito.given(jpaCommentRepository.findAllByTodo_TodoId(any(Long.class),any(Pageable.class))).willReturn(commentsPage);

        //when
        Api<List<SimpleCommentRes>> ret =  simpleCommentService.findComments(1L , pageable);

        //then
        List<SimpleCommentRes> commentRes = commentsPage.getContent().stream().map(i -> new SimpleCommentRes(i.getTodoId(), i.getComment(), i.getUserName())).toList();
        Assertions.assertThat(ret.getBody()).usingRecursiveComparison().isEqualTo(commentRes);

        Assertions.assertThat(ret.getPagination()).usingRecursiveComparison().isEqualTo(pagination);

        BDDMockito.verify(jpaCommentRepository).findAllByTodo_TodoId(1L, pageable);

    }

    @Test
    @DisplayName("댓글_수정_성공")
    void updateComment_성공() {
       //given
        Todo todo = new Todo();
        Comment comment = new Comment(todo, "comments", "userName");
        UpdateCommentReq req = new UpdateCommentReq("updatedComments");
        BDDMockito.given(jpaCommentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        //when
        SimpleCommentRes res =  simpleCommentService.updateComment(1L ,req);
        //then
        Assertions.assertThat(res.getComments()).isEqualTo("updatedComments");
        Assertions.assertThat(res.getUserName()).isEqualTo("userName");
    }

    @Test
    @DisplayName("댓글_수정_실패")
    void updateComment_실패() {
        //given
        UpdateCommentReq req = new UpdateCommentReq("updateComments");
        BDDMockito.given(jpaCommentRepository.findById(anyLong())).willReturn(Optional.empty());

        CommentException commentException = assertThrows(CommentException.class, () -> simpleCommentService.updateComment(1L, req));

        Assertions.assertThat(commentException.getStatus()).isEqualTo(CommentErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(commentException.getMessage()).isEqualTo(CommentErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글_삭제_성공")
    void deleteComment_성공() {
        Long commentId = 1L;

        BDDMockito.given(jpaCommentRepository.existsById(anyLong())).willReturn(true);

        Map<String, String> res = simpleCommentService.deleteComment(commentId);

        Assertions.assertThat(res.get("message")).isEqualTo("댓글이 삭제되었습니다.");

        BDDMockito.verify(jpaCommentRepository).deleteById(1L);
        BDDMockito.verify(jpaCommentRepository).existsById(1L);
    }

    @Test
    @DisplayName("댓글_삭제_실패")
    void deleteComment_실패() {

        BDDMockito.given(jpaCommentRepository.existsById(anyLong())).willReturn(false);

        CommentException commentException = assertThrows(CommentException.class,
                () -> simpleCommentService.deleteComment(1L));

        Assertions.assertThat(commentException.getStatus()).isEqualTo(CommentErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(commentException.getMessage()).isEqualTo(CommentErrorCode.NOT_FOUND.getMessage());

        BDDMockito.verify(jpaCommentRepository).existsById(1L);
        BDDMockito.verify(jpaCommentRepository, Mockito.never()).deleteById(1L);
    }
}
