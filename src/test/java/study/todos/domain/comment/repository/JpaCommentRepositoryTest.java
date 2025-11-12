package study.todos.domain.comment.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.List;
import java.util.stream.IntStream;

@DataJpaTest
public class JpaCommentRepositoryTest {

    @Autowired
    JpaCommentRepository jpaCommentRepository;

    @Autowired
    JpaTodoRepository jpaTodoRepository;

    @Test
    @DisplayName("댓글_저장")
    void saveComment() {

        Todo todo = jpaTodoRepository.save(new Todo());

        Comment comment = new Comment(todo, "comment", "jamni");

        Comment save = jpaCommentRepository.save(comment);


        Assertions.assertThat(save.getComment()).isEqualTo("comment");
        Assertions.assertThat(save.getUserName()).isEqualTo("jamni");

    }

    @Test
    @DisplayName("댓글_조회_성공")
    void findComment() {
        //given
        Todo todo = jpaTodoRepository.save(new Todo());
        Comment comment = new Comment(todo, "comments", "jamni");
        Comment saveComment = jpaCommentRepository.save(comment);

        //when
        Comment foundcomment = jpaCommentRepository.findById(saveComment.getCommentId()).get();

        //then
        Assertions.assertThat(foundcomment.getComment()).isEqualTo("comments");
        Assertions.assertThat(foundcomment.getUserName()).isEqualTo("jamni");
    }

    @Test
    @DisplayName("댓글_전체_조회")
    void findComments() {
        //given
        Todo todo = new Todo();
        Todo save = jpaTodoRepository.save(todo);
        IntStream.range(1,12).forEach( i -> jpaCommentRepository.save(new Comment(save, "comment" + i, "userName")));
                                                                                    //Long.parseLong( i + "")
        List<Comment> comments = IntStream.range(1, 11).mapToObj(i -> new Comment(Long.valueOf(i), save, "comment" + i, "userName")).toList();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Comment> commentsByTodoId = jpaCommentRepository.findAllByTodo_TodoId(1L, pageable);

        Assertions.assertThat(commentsByTodoId.getContent()).usingRecursiveComparison().isEqualTo(comments);
        Assertions.assertThat(commentsByTodoId.getTotalElements()).isEqualTo(11);
        Assertions.assertThat(commentsByTodoId.hasNext()).isTrue();
    }
}

//usingRecursiveComparison() => 재귀적 필드 비교, 내부의 실제 필드 값들을 비교
// 재귀적 필드 비교: 객체뿐만 아니라 객체 안에 중첩된 객체들까지 모든 검사