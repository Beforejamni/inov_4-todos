package study.todos.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.todos.domain.comment.entitiy.Comment;

public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {

    @Query("select c from Comment c where c.todo.todoId = :todoId")
    Page<Comment> findAllByTodoId(@Param("todoId") Long todoId , Pageable pageable);
}

//EntityGraph -> LEFT OUTER JOIN
//Query -> INNER JOIN or LEFT OUTER JOIN 선택적으로 사용