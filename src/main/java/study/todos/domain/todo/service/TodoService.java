package study.todos.domain.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.dto.UpdateTodoReq;

import java.util.Map;

//게시글 삭제시 관련 댓글 삭제를 데코레이터 패턴으로 예상
public interface TodoService {

    SimpleTodoRes saveTodo(SimpleTodoReq req);

    SimpleTodoRes findTodo(Long todoId);

    Page<SimpleTodoRes> findTodos(Pageable pageable);

    SimpleTodoRes updateTodo(Long todoId, UpdateTodoReq req);

    Map<String, String> deleteTodo(Long todoId);
}
