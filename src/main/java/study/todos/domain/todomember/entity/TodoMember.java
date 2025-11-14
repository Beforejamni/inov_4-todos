package study.todos.domain.todomember.entity;

import jakarta.persistence.*;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.todo.entity.Todo;

@Entity
public class TodoMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoMemberId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    protected TodoMember() {
    }

    public TodoMember(Todo todo, Member member) {
        this.todo = todo;
        this.member = member;
    }

    public Long getTodoMemberId() {
        return todoMemberId;
    }

    public Member getMember() {
        return member;
    }

    public Todo getTodo() {
        return todo;
    }
}
