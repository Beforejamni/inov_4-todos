package study.todos.domain.todo.exception;

public class TodoException extends RuntimeException {

    private final int status;

    public TodoException(TodoErrorCode errorCode){
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

    public int getStatus() {
        return status;
    }
}
