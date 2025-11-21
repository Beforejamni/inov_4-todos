package study.todos.domain.auth.dto;

public class SimpleAuthRes {
    private final String username;
    private final String message;

    public SimpleAuthRes(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
