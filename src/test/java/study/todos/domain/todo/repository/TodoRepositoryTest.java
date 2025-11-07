package study.todos.domain.todo.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import study.todos.domain.todo.entity.Todo;

import java.util.Optional;

//DataJpaTest에 @Transactional이 있음
@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    JpaTodoRepository jpaTodoRepository;


    @Test
    @DisplayName("Todo_저장_성공")
    void saveTodo() {

        //given
        Todo todo = new Todo("jamni","title","contents");

        //when
        jpaTodoRepository.save(todo);
        Todo savedTodo = jpaTodoRepository.findById(1L).get();

        //then
        Assertions.assertThat(savedTodo.getUserName()).isEqualTo("jamni");
        Assertions.assertThat(savedTodo.getTitle()).isEqualTo("title");
        Assertions.assertThat(savedTodo.getContent()).isEqualTo("contents");
    }

    @Test
    @DisplayName("일정_단건_조회_성공")
    void findTodo_성공() {

        Todo todo = new Todo("jamni","title","contents");

        Todo savedTodo = jpaTodoRepository.saveAndFlush(todo);

        Optional<Todo> foundTodo = jpaTodoRepository.findById(savedTodo.getTodoId());

        Assertions.assertThat(foundTodo.get().getTodoId()).isEqualTo(savedTodo.getTodoId());
        Assertions.assertThat(foundTodo.get().getUserName()).isEqualTo("jamni");
        Assertions.assertThat(foundTodo.get().getTitle()).isEqualTo("title");
        Assertions.assertThat(foundTodo.get().getContent()).isEqualTo("contents");
    }

    @Test
    @DisplayName("일정_단건_조회_실패")
    void findTodo_실패() {
        Optional<Todo> foundTodo = jpaTodoRepository.findById(1L);
        Assertions.assertThat(foundTodo).isEmpty();
    }

    @Test
    @DisplayName("일정_전체_조회_성공")
    void findTodos_성공() {

        //given
        for(int i = 1; i<= 13; i++) {
            Todo todo = Todo.createTodo("test" + i, "title" + i, "content" + i);
            jpaTodoRepository.save(todo);
        }

        //when
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "todoId"));
        Page<Todo> todos = jpaTodoRepository.findAll(pageable);

        //then
        Assertions.assertThat(todos.getNumber()).isEqualTo(0);
        Assertions.assertThat(todos.getSize()).isEqualTo(size);
        Assertions.assertThat(todos.getNumberOfElements()).isEqualTo(10);
        Assertions.assertThat(todos.hasNext()).isTrue();

    }

    @Test
    @DisplayName("일정_전체_다음페이지_성공")
    void findTodosNext_성공() {

        for(int i = 1; i<= 13; i++) {
            Todo todo = Todo.createTodo("test" + i, "title" + i, "content" + i);
            jpaTodoRepository.save(todo);
        }

        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "todoId"));
        Page<Todo> todos = jpaTodoRepository.findAll(pageable.next());


        Assertions.assertThat(todos.getNumber()).isEqualTo(1);
        Assertions.assertThat(todos.getSize()).isEqualTo(size);
        Assertions.assertThat(todos.getNumberOfElements()).isEqualTo(3);

    }

    @Test
    @DisplayName("일정_삭제_성공")
    void deleteTodo_성공() {
        Todo todo = new Todo("jamni", "title", "contents");

        Todo save = jpaTodoRepository.save(todo);

        jpaTodoRepository.delete(save);

        Optional<Todo> foundTodo = jpaTodoRepository.findById(1L);

        Assertions.assertThat(foundTodo).isEmpty();
    }
}