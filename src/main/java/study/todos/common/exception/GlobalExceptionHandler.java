package study.todos.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoException.class)
    public ResponseEntity<Map<String, String>> handlerTodoException(TodoException e){
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }
}
