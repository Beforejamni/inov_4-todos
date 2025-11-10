package study.todos.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import study.todos.domain.comment.exception.CommentException;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoException.class)
    public ResponseEntity<Map<String, String>> handlerTodoException(TodoException e){
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(CommentException.class)
    public  ResponseEntity<Map<String, String>> handlerCommentException(CommentException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
    }
}
