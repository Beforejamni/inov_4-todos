package study.todos.domain.comment.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.todos.domain.comment.entitiy.Comment;
import study.todos.domain.todo.entity.Todo;

public class CommentTest {

    @Test
    @DisplayName("댓글_객체_생성")
    void createComment() {
        Todo todo = new Todo();

        Comment comment = new Comment(todo ,"comments", "jamni");

        Assertions.assertThat(comment.getComment()).isEqualTo("comments");
        Assertions.assertThat(comment.getUserName()).isEqualTo("jamni");
    }
}
