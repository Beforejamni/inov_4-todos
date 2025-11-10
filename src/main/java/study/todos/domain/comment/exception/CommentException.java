package study.todos.domain.comment.exception;

public class CommentException extends RuntimeException {

    private final int status;

    public CommentException(CommentErrorCode errorCode) {
      super(errorCode.getMessage());
      this.status = errorCode.getStatus();
    }

  public int getStatus() {
    return status;
  }
}
