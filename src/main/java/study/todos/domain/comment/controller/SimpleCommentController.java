package study.todos.domain.comment.controller;


import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.todos.common.dto.Api;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.service.CommentService;

import java.util.List;

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

    @GetMapping("/{todoId}")
    public ResponseEntity<Api<List<SimpleCommentRes>>> findComments(@PathVariable Long todoId,
                                                                    @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commentService.findComments(todoId, pageable));
    }
}
