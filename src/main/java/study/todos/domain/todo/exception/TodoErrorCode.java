package study.todos.domain.todo.exception;

public enum TodoErrorCode {

    NOT_FOUND(404,"일정을 찾을 수 없습니다.");

    private final int status;
    private final String message;

     TodoErrorCode(int status, String message){
        this.status = status;
        this. message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
