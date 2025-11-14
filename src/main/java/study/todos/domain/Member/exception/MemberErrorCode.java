package study.todos.domain.Member.exception;

public enum MemberErrorCode {
    NOT_FOUND(404, "유저를 찾을 수 없습니다.");

    private final int status;
    private final String message;

     MemberErrorCode(int status, String message){
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
