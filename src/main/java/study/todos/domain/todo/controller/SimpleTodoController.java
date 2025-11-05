package study.todos.domain.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
import study.todos.domain.todo.service.SimpleTodoService;
import study.todos.domain.todo.service.TodoService;

@RestController
@RequestMapping("/todos")
public class SimpleTodoController {


    private final TodoService todoService;

    public SimpleTodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<SimpleTodoRes> savetodo(@RequestBody SimpleTodoReq req) {
        return new ResponseEntity<>(todoService.saveTodo(req), HttpStatus.CREATED);
    }
}
