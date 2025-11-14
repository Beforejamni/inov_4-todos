package study.todos.domain.todo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import study.todos.domain.todo.dto.UpdateTodoReq;
import study.todos.domain.todomember.entity.TodoMember;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @CreatedDate //엔티티가 최초로 생성되어 저장!!될 때 시간이 자동 저장됨
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @LastModifiedDate //조회한 엔티티의 값을 수정!!할 때 시간이 자동 저장됨
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "todo", fetch = FetchType.LAZY)
    private List<TodoMember> members = new ArrayList<>();

//  업데이트 추가 발생/제약 관리로 인해 단방향 진행
//    //cascade = 영속성 전이 기능
//    //ophanRemoval = 고아 객체 자동 삭제
//    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();

    public Todo() {}

    public Todo(String userName, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Todo (String userName, String title, String content) {
        this.userName = userName;
        this.title = title;
        this.content = content;
    }

    public static Todo createTodo(String userName, String title, String content) {
        return new Todo(userName, title, content);
    }

    public void updateTodo(UpdateTodoReq req){
        this.title = (req.title() == null) ? this.title : req.title();
        this.content = (req.content() == null) ? this.content : req.content();
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

    public void setId(long todoId) {
        this.todoId = todoId;
    }
}
