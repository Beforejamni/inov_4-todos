package study.todos.domain.service;

import org.springframework.stereotype.Service;
import study.todos.domain.comment.dto.SimpleCommentReq;
import study.todos.domain.comment.dto.SimpleCommentRes;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.comment.repository.JpaCommentRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.exception.TodoErrorCode;
import study.todos.domain.todo.exception.TodoException;
import study.todos.domain.todo.repository.JpaTodoRepository;

@Service
public class SimpleCommentService implements CommentService{

    private final JpaCommentRepository commentRepository;
    private final JpaTodoRepository todoRepository;

    public SimpleCommentService(JpaCommentRepository commentRepository, JpaTodoRepository todoRepository) {
        this.commentRepository = commentRepository;
        this.todoRepository = todoRepository;
    }

    @Override
    public SimpleCommentRes save(Long TodoId, SimpleCommentReq req) {

        Todo todo = todoRepository.findById(TodoId).orElseThrow(() -> new TodoException(TodoErrorCode.NOT_FOUND));

        Comment comment = new Comment(todo, req.comments(), req.userName());
        Comment save = commentRepository.save(comment);

        return new SimpleCommentRes(save.getTodoId(), save.getComment(), save.getUserName());
    }
}
