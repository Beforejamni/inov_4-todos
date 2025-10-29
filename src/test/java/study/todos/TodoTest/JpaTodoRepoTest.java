package study.todos.TodoTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import study.todos.Todo;
import study.todos.repository.TodoRepository;

import java.util.Optional;


@DataJpaTest
@Transactional
public class JpaTodoRepoTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    @DisplayName("Todo 저장")
    void saveTodo() {

        Todo todo = Todo.createTodo("jamni", "제목", "내용");

        Todo saveTodo = todoRepository.save(todo);

        Assertions.assertThat(saveTodo).isNotNull();
        Assertions.assertThat(saveTodo.getTodoId()).isNotNull();

        Optional<Todo> OpTodo = todoRepository.findByTodoId(saveTodo.getTodoId());

        Assertions.assertThat(OpTodo).isPresent();

        Todo findTodo = OpTodo.get();

        Assertions.assertThat(findTodo.getTodoId()).isEqualTo(1L);
        Assertions.assertThat(findTodo.getUserName()).isEqualTo("jamni");
        Assertions.assertThat(findTodo.getTitle()).isEqualTo("제목");
        Assertions.assertThat(findTodo.getContent()).isEqualTo("내용");
        Assertions.assertThat(findTodo.getCreatedAt()).isEqualTo(findTodo.getUpdatedAt());
    }

}
