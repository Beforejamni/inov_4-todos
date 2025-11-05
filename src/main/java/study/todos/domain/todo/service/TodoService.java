package study.todos.domain.todo.service;

import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;


public interface TodoService {

    SimpleTodoRes saveTodo(SimpleTodoReq req);

    SimpleTodoRes findTodo(Long id);
}
