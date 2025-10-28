package study.todos.TodoTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import study.todos.Todo;

import java.time.*;

public class TodoTest {


    @Test
    void createTodo() {

        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        Todo todo = Todo.createTodo("Jamni", "Title", "Contents", fixed);

        LocalDateTime time = LocalDateTime.now(fixed);

        Assertions.assertThat(todo.getUserName()).isEqualTo("Jamni");
        Assertions.assertThat(todo.getTitle()).isEqualTo("Title");
        Assertions.assertThat(todo.getContent()).isEqualTo("Contents");
        Assertions.assertThat(todo.getCreatedAt()).isEqualTo(time);
        Assertions.assertThat(todo.getUpdatedAt()).isEqualTo(time);
    }
}
