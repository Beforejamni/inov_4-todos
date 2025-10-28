package study.todos;

import java.time.Clock;
import java.time.LocalDateTime;

public class Todo {
    private Long todoId;

    private String userName;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Todo() {}

    public Todo (String userName, String title, String content, Clock clock) {
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now(clock);
        this.updatedAt = LocalDateTime.now(clock);
    }

    public static Todo createTodo(String userName, String title, String content, Clock clock) {
        return new Todo(userName, title, content, clock);
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
