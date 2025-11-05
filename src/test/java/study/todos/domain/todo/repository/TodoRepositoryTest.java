package study.todos.domain.todo.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.todo.entity.Todo;

@DataJpaTest
@Transactional
public class TodoRepositoryTest {

    @Autowired
    JpaTodoRepository jpaTodoRepository;


    @Test
    @DisplayName("Todo_저장_성공")
    void saveTodo() {

        //given
        Todo todo = new Todo("jamni","title","contents");

        //when
        jpaTodoRepository.save(todo);
        Todo savedTodo = jpaTodoRepository.findById(1L).get();

        //then
        Assertions.assertThat(savedTodo.getUserName()).isEqualTo("jamni");
        Assertions.assertThat(savedTodo.getTitle()).isEqualTo("title");
        Assertions.assertThat(savedTodo.getContent()).isEqualTo("contents");
    }

}