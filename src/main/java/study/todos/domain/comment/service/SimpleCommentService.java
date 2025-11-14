package study.todos.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.dto.Api;
import study.todos.common.dto.Pagination;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.dto.UpdateCommentReq;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.comment.exception.CommentErrorCode;
import study.todos.domain.comment.exception.CommentException;
import study.todos.domain.comment.repository.JpaCommentRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SimpleCommentService implements CommentService{

    private final JpaCommentRepository commentRepository;
    private final JpaTodoRepository todoRepository;
    private final JpaCommentRepository jpaCommentRepository;

    public SimpleCommentService(JpaCommentRepository commentRepository, JpaTodoRepository todoRepository, JpaCommentRepository jpaCommentRepository) {
        this.commentRepository = commentRepository;
        this.todoRepository = todoRepository;
        this.jpaCommentRepository = jpaCommentRepository;
    }

    @Override
    @Transactional
    public SimpleCommentRes save(Long TodoId, SimpleCommentReq req) {

        Todo todo = todoRepository.findById(TodoId).orElseThrow(() -> new TodoException(TodoErrorCode.NOT_FOUND));

        Comment comment = new Comment(todo, req.comments(), req.userName());
        Comment save = commentRepository.save(comment);

        return new SimpleCommentRes(save.getTodoId(), save.getComment(), save.getUserName());
    }

    @Override
    @Transactional(readOnly = true)
    public SimpleCommentRes findComment(Long commentId) {

        Comment comment = jpaCommentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));

        return new SimpleCommentRes(comment.getTodoId(), comment.getComment(), comment.getUserName());
    }

    @Override
    @Transactional(readOnly = true)
    public  Api<List<SimpleCommentRes>> findComments(Long todoId, Pageable pageable) {

        Page<Comment> commentsByTodoId = jpaCommentRepository.findAllByTodo_TodoId(todoId, pageable);

        List<Comment> content = commentsByTodoId.getContent();

        List<SimpleCommentRes> commentResList = content.stream().map(i -> new SimpleCommentRes(i.getTodoId(), i.getComment(), i.getUserName())).toList();

        Pagination pagination = new Pagination(pageable.getPageNumber(),
                                                pageable.getPageSize(),
                                                commentsByTodoId.getNumberOfElements(),
                                                commentsByTodoId.getTotalPages(),
                                                commentsByTodoId.getTotalElements());

        return new Api<>(commentResList, pagination);
    }

    @Override
    @Transactional
    public SimpleCommentRes updateComment(Long commentId, UpdateCommentReq req) {

        Comment comment = jpaCommentRepository.findById(commentId).orElseThrow(() -> new CommentException(CommentErrorCode.NOT_FOUND));

        comment.update(req);

        return new SimpleCommentRes(comment.getTodoId(), comment.getComment(), comment.getUserName());
    }

    @Override
    public Map<String, String> deleteComment(Long commentId) {

        if(!jpaCommentRepository.existsById(commentId)) {
            throw new CommentException(CommentErrorCode.NOT_FOUND);
        }
        jpaCommentRepository.deleteById(commentId);

        return Map.of("message", "댓글이 삭제되었습니다.");
    }


}
