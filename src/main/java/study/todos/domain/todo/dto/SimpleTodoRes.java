package study.todos.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import study.todos.domain.todo.entity.Todo;

import java.time.LocalDateTime;

public record SimpleTodoRes(String userName, String title, String content, LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
    @JsonCreator // 해당 함수를 통해 객체 생성 필드를 생성 동시에 채움
    public SimpleTodoRes(@JsonProperty("userName") String userName,
                         @JsonProperty("title") String title,
                         @JsonProperty("contents") String content,
                         @JsonProperty("createdAt") LocalDateTime createdAt,
                         @JsonProperty("updatedAt") LocalDateTime updatedAt) {
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SimpleTodoRes toDto(Todo todo){
        return new SimpleTodoRes(todo.getUserName(),
                todo.getTitle(),
                todo.getContent(),
                todo.getCreatedAt(),
                todo.getUpdatedAt());
    }
}
