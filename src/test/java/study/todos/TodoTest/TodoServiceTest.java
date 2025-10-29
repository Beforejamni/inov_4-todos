package study.todos.TodoTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.todos.Todo;
import study.todos.repository.TodoRepository;
import study.todos.service.CreateTodoReq;
import study.todos.service.SimpleTodoService;
import study.todos.service.TodoService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TodoServiceTest {


    @Autowired
    TodoService todoService;

    @Test
    @DisplayName("Service save Todo")
    void saveTodo() {


        CreateTodoReq createTodoReq = new CreateTodoReq("jamni", "제목", "내용");

        Todo todo = todoService.saveTodo(createTodoReq);

        Assertions.assertThat(todo.getTodoId()).isEqualTo(1L);
        Assertions.assertThat(todo.getUserName()).isEqualTo("jamni");
        Assertions.assertThat(todo.getTitle()).isEqualTo("제목");
        Assertions.assertThat(todo.getContent()).isEqualTo("내용");
    }
}
