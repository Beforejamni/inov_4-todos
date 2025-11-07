package study.todos.domain.todo.Controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.todos.domain.todo.controller.SimpleTodoController;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.dto.UpdateTodoReq;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.service.TodoService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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

    @Test
    @DisplayName("Todo_조회_성공")
    void findTodo() {

        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now(fixed);
        SimpleTodoRes res = new SimpleTodoRes("jamni", "title", "contents", now, now);

        BDDMockito.given(todoService.findTodo(1L)).willReturn(res);

        ResponseEntity<SimpleTodoRes> foundTodo = todoController.findTodo(1L);

        Assertions.assertThat(foundTodo.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(foundTodo.getBody().userName()).isEqualTo("jamni");
        Assertions.assertThat(foundTodo.getBody().title()).isEqualTo("title");
        Assertions.assertThat(foundTodo.getBody().content()).isEqualTo("contents");

    }

    @Test
    @DisplayName("Todo_조회_실패")
    void findTodo_실패() {
        BDDMockito.given(todoService.findTodo(any(Long.class))).willThrow(new TodoException(TodoErrorCode.NOT_FOUND));

        assertThrows(TodoException.class, () -> todoController.findTodo(1L));

        BDDMockito.then(todoService).should().findTodo(1L);
        BDDMockito.then(todoService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Todo_전체_조회_성공")
    void findTodos_성공(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size , Sort.by(Sort.Direction.ASC, "todoId"));


        List<SimpleTodoRes> todos = IntStream.rangeClosed(1, 12).mapToObj(i -> new SimpleTodoRes("jamni", "" + i, "contents", null, null))
                .limit(size)
                .toList();

        PageImpl<SimpleTodoRes> simpleTodoRes = new PageImpl<>(todos, pageable, 12);

        BDDMockito.given(todoService.findTodos(pageable)).willReturn(simpleTodoRes);

        ResponseEntity<Page<SimpleTodoRes>> response = todoController.findTodos(pageable);

        //then
        BDDMockito.verify(todoService).findTodos(pageable);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getNumber()).isEqualTo(0);
        Assertions.assertThat(response.getBody().getSize()).isEqualTo(10);
        Assertions.assertThat(response.getBody().getTotalElements()).isEqualTo(12);
        Assertions.assertThat(response.getBody().hasNext()).isTrue();
        Assertions.assertThat(response.getBody().getContent().get(0).title()).isEqualTo("1");

    }

    @Test
    @DisplayName("Todo_업데이트")
    void updateTodo_성공() {
        //given
        UpdateTodoReq req = new UpdateTodoReq(null, "updated");
        SimpleTodoRes response = new SimpleTodoRes("jamni", "title", "updated", null, null);

        //하드코딩 금지
        BDDMockito.given(todoService.updateTodo(any(Long.class),any(UpdateTodoReq.class))).willReturn(response);
        //when

        ResponseEntity<SimpleTodoRes> responseEntity = todoController.updateTodo(1L, req);

        SimpleTodoRes body = responseEntity.getBody();
        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(body.title()).isEqualTo("title");
        Assertions.assertThat(body.content()).isEqualTo("updated");

    }
}
