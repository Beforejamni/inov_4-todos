package study.todos.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.todos.domain.comment.entitiy.Comment;

public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {
}
