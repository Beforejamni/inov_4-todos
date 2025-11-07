package study.todos.domain.comment.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

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
}
