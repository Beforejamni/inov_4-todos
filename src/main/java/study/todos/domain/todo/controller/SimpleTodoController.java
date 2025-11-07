package study.todos.domain.todo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.todos.domain.todo.dto.SimpleTodoReq;
import study.todos.domain.todo.dto.SimpleTodoRes;
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

    @GetMapping("/{todoId}")
    public ResponseEntity<SimpleTodoRes> findTodo(@PathVariable Long todoId) {
        return ResponseEntity.ok(todoService.findTodo(todoId));
    }

    @GetMapping()
    public ResponseEntity<Page<SimpleTodoRes>> findTodos(@PageableDefault()Pageable pageable) {

        return ResponseEntity.ok(todoService.findTodos(pageable));
    }
}
