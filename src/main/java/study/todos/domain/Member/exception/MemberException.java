package study.todos.domain.Member.exception;

public class MemberException extends RuntimeException {
  private final int status;

  public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

  public int getStatus() {
    return status;
  }
}
