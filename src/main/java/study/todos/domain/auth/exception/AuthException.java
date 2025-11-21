package study.todos.domain.auth.exception;

public class AuthException extends RuntimeException {

    private int status;

    public AuthException(AuthErrorCode errorCode) {
      super(errorCode.getMessage());
      this.status = errorCode.getStatus();
    }

  public int getStatus() {
    return status;
  }
}
