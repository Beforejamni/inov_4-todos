package study.todos.domain.comment.service;

import org.springframework.data.domain.Pageable;
import study.todos.common.dto.Api;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.dto.UpdateCommentReq;
import study.todos.domain.comment.entitiy.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    SimpleCommentRes save(Long TodoId, SimpleCommentReq req);

    SimpleCommentRes findComment(Long commentId);

    Api<List<SimpleCommentRes>> findComments(Long todoId, Pageable pageable);

    SimpleCommentRes updateComment(Long commentId, UpdateCommentReq req);

    Map<String, String> deleteComment(Long commentId);
}
