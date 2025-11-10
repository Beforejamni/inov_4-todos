package study.todos.domain.comment.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.Optional;

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
}
