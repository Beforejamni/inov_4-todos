package study.todos.domain.auth.exception;

public enum AuthErrorCode {
    DUPLICATION(409, "이미 존재하는 아이디입니다.");

    private final int status;
    private final String message;

  AuthErrorCode(int status, String message){
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
