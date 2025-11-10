package study.todos.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.service.CommentService;

@RestController
@RequestMapping("/comments")
public class SimpleCommentController {

    private final CommentService commentService;

    public SimpleCommentController( CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/{todoId}")
    public ResponseEntity<SimpleCommentRes> save(@PathVariable Long todoId,
                                                 @RequestBody SimpleCommentReq req) {
        return new ResponseEntity<>( commentService.save(todoId, req), HttpStatus.CREATED);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<SimpleCommentRes> findComment(@PathVariable Long commentId) {

        return ResponseEntity.ok(commentService.findComment(commentId));
    }
}
