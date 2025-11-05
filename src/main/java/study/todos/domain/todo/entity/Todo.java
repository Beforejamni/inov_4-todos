package study.todos.domain.todo.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;


    @Column(name = "user_name")
    private String userName;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;


    @CreatedDate //엔티티가 최초로 생성되어 저장될 때 시간이 자동 저장됨
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @LastModifiedDate //조회한 엔티티의 값을 수정할 때 시간이 자동 저장됨
    @Column(name = "update_at")
    private LocalDateTime updatedAt;


    public Todo() {}

    public Todo (String userName, String title, String content) {
        this.userName = userName;
        this.title = title;
        this.content = content;
    }

    public static Todo createTodo(String userName, String title, String content) {
        return new Todo(userName, title, content);
    }

    public Long getTodoId() {
        return todoId;
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
