package study.todos.domain.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.dto.UpdateTodoReq;


public interface TodoService {

    SimpleTodoRes saveTodo(SimpleTodoReq req);

    SimpleTodoRes findTodo(Long todoId);

    Page<SimpleTodoRes> findTodos(Pageable pageable);

    SimpleTodoRes updateTodo(Long todoId, UpdateTodoReq req);
}
