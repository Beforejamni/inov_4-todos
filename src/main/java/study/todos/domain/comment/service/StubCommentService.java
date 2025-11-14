package study.todos.domain.comment.service;

import org.springframework.data.domain.Pageable;
import study.todos.common.dto.Api;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.dto.UpdateCommentReq;

import java.util.List;
import java.util.Map;

public class StubCommentService implements CommentService{
    @Override
    public SimpleCommentRes save(Long TodoId, SimpleCommentReq req) {
        return new SimpleCommentRes(1L,"comments", "userName");
    }

    @Override
    public SimpleCommentRes findComment(Long commentId) {
        return null;
    }

    @Override
    public Api<List<SimpleCommentRes>> findComments(Long todoId, Pageable pageable) {
        return null;
    }

    @Override
    public SimpleCommentRes updateComment(Long commentId, UpdateCommentReq req) {
        return null;
    }

    @Override
    public Map<String, String> deleteComment(Long commentId) {
        return Map.of();
    }
}
