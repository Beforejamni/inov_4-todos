package study.todos.domain.todo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.repository.JpaTodoRepository;

@ExtendWith(MockitoExtension.class)
public class SimpleTodoServiceTest{


    @Mock
    JpaTodoRepository jpaTodoRepository;

    @Test
    void saveTodo_성공(){

        SimpleTodoReq todo = new SimpleTodoReq("jamni", "제목", "내용");

        SimpleTodoService simpleTodoService = new SimpleTodoService(jpaTodoRepository);

        SimpleTodoRes res = simpleTodoService.saveTodo(todo);

        Assertions.assertThat(res.userName()).isEqualTo("jamni");
        Assertions.assertThat(res.title()).isEqualTo("제목");
        Assertions.assertThat(res.content()).isEqualTo("내용");

    }

}