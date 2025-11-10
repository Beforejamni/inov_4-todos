package study.todos.domain.comment.exception;

public enum CommentErrorCode {
    NOT_FOUND(404, "댓글을 찾을 수 없습니다.");

    private final int status;
    private final String message;
    CommentErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
