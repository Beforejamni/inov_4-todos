package study.todos.domain.service;

import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;

public interface CommentService {

    SimpleCommentRes save(Long TodoId, SimpleCommentReq req);
}
