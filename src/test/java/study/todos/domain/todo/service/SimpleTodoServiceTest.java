package study.todos.domain.todo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
@Transactional
public class SimpleTodoServiceTest{


    @Mock
    JpaTodoRepository jpaTodoRepository;

    @InjectMocks
    SimpleTodoService simpleTodoService;

    @Test
    void saveTodo_성공(){

        SimpleTodoReq req = new SimpleTodoReq("jamni", "제목", "내용");

        Todo todo = new Todo(req.userName(), req.title(), req.content());

        //동일한 인스턴스를 스텁할 수 없어 any(Todo.class)로 지정
        Mockito.when(jpaTodoRepository.save(any(Todo.class))).thenReturn(todo);

        SimpleTodoRes res = simpleTodoService.saveTodo(req);


        assertThat(res.userName()).isEqualTo("jamni");
        assertThat(res.title()).isEqualTo("제목");
        assertThat(res.content()).isEqualTo("내용");

    }

    @Test
    @DisplayName("일정_단건_조회_성공")
    void findTodo_성공() {

        LocalDateTime dateTime = LocalDateTime.now().withNano(0);

        Todo todo = new Todo("jamni", "title", "contents", dateTime, dateTime);

        given(jpaTodoRepository.findById(1L)).willReturn(Optional.of(todo));

        SimpleTodoRes res = simpleTodoService.findTodo(1L);

        assertThat(res.userName()).isEqualTo("jamni");
        assertThat(res.title()).isEqualTo("title");
        assertThat(res.content()).isEqualTo("contents");
        assertThat(res.createdAt()).isEqualTo(dateTime);
        assertThat(res.updatedAt()).isEqualTo(dateTime);

        then(jpaTodoRepository).should().findById(1L);
    }

    @Test
    @DisplayName("일정_단건_조회_실패")
    void findTodo_실패() {

        given(jpaTodoRepository.findById(1L)).willReturn(Optional.empty());

        TodoException exception = assertThrows(TodoException.class,
                () -> simpleTodoService.findTodo(1L));

        assertThat(exception.getStatus()).isEqualTo(404);
    }

}