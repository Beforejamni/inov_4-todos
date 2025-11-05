package study.todos.domain.todo.Controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.todos.domain.todo.controller.SimpleTodoController;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.service.TodoService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
public class SimpleTodoControllerUnitTest {

    @Mock
    TodoService todoService;

    @InjectMocks
    SimpleTodoController todoController;

    @Test
    @DisplayName("Todo_저장_성공")
    void saveTodo() {
        SimpleTodoReq req = new SimpleTodoReq("jamni", "title", "contents");

        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now(fixed);
        SimpleTodoRes res = new SimpleTodoRes("jamni", "title", "contents", now, now);

        Mockito.when(todoService.saveTodo(req)).thenReturn(res);

        ResponseEntity<SimpleTodoRes> savetodo = todoController.savetodo(req);

        Assertions.assertThat(savetodo.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(savetodo.getBody().userName()).isEqualTo("jamni");
        Assertions.assertThat(savetodo.getBody().title()).isEqualTo("title");

        //메서드 호출 여부 확인
        Mockito.verify(todoService).saveTodo(req);
    }
}
