package study.todos.domain.comment.dto;

public class SimpleCommentRes {
    private final Long todoId;
    private final String comments;
    private final String userName;

    public SimpleCommentRes(Long todoId, String comments, String userName) {
        this.todoId = todoId;
        this.comments = comments;
        this.userName = userName;
    }

    public Long getTodoId() {
        return todoId;
    }

    public String getComments() {
        return comments;
    }

    public String getUserName() {
        return userName;
    }
}
