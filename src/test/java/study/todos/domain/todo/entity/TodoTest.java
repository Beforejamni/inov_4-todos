package study.todos.domain.todo.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TodoTest{

    @Test
    @DisplayName("Todo_생성_성공")
    void createTodo() {
        Todo todo = new Todo("jamni","title","contents");

        Assertions.assertThat(todo.getUserName()).isEqualTo("jamni");
        Assertions.assertThat(todo.getTitle()).isEqualTo("title");
        Assertions.assertThat(todo.getContent()).isEqualTo("contents");
    }
}