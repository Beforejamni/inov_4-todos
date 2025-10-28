package study.todos.TodoTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import study.todos.Todo;

import java.time.*;

public class TodoTest {


    @Test
    void createTodo() {
        Todo todo = Todo.createTodo("Jamni", "Title", "Contents");

        Assertions.assertThat(todo.getUserName()).isEqualTo("Jamni");
        Assertions.assertThat(todo.getTitle()).isEqualTo("Title");
        Assertions.assertThat(todo.getContent()).isEqualTo("Contents");
    }
}
