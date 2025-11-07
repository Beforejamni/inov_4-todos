package study.todos.domain.todo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@Transactional
public class SimpleTodoServiceTest{


    @Mock
    JpaTodoRepository jpaTodoRepository;

    @InjectMocks
    SimpleTodoService simpleTodoService;

    @Test
    @DisplayName("일정_저장_성공")
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

    @Test
    @DisplayName("일정_전체_조회_성공")
    void findTodoAll_성공() {

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size , Sort.by(Sort.Direction.ASC, "todoId"));

        List<Todo> todoList = new ArrayList<>();

        for(int i = 1; i <= 12; i++) {
            Todo todo = Todo.createTodo("jamni", "title" + i, "contents" + i);
            todoList.add(todo);
        }

        PageImpl<Todo> todos = new PageImpl<>(todoList.subList(0, size), pageable, todoList.size());

        //jpaTodoRepository.findAll()를 호출하면 , todos를 반환해준다.
        given(jpaTodoRepository.findAll(any(Pageable.class))).willReturn(todos);

        //simpleTodoService.findTodo 를 실행한다.
        Page<SimpleTodoRes> results = simpleTodoService.findTodos(pageable);

        //상태를 검증한다.
        Assertions.assertThat(results.getNumber()).isEqualTo(0);
        Assertions.assertThat(results.getSize()).isEqualTo(10);
        Assertions.assertThat(results.getNumberOfElements()).isEqualTo(10);
        Assertions.assertThat(results.getTotalElements()).isEqualTo(12);
        Assertions.assertThat(results.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(results.hasNext()).isTrue();


        verify(jpaTodoRepository).findAll(pageable);

        Assertions.assertThat(pageable).isEqualTo(results.getPageable());
    }

}


//Mockito -> DI를 지원해준다.
//Mockito -> Mock 객체를 직접 만들고 관리해주는 테스트 프레임워크
//Mock 객체 -> 원하는 동작만 하도록 만드는 것

//TDD -> 기술적인 테스트 케이스를 먼저 작성하고 이를 통과하는 코드를 작성
//BDD -> given,when,then 패턴으로 시나리오를 먼저 작성하고, 이를 테스트하고 구현


//BDD(Behavior-Driven Development) => 행위 주도 개발
//BDDMockito
//given
//when
//then
