package study.todos.domain.todomember.dto;

import study.todos.domain.Member.entity.Member;
import study.todos.domain.todo.entity.Todo;

public class SimpleMemberTodoRes {
    private Todo todo;
    private Member member;

    public SimpleMemberTodoRes(Todo todo, Member member) {
        this.todo = todo;
        this.member = member;
    }

    public Todo getTodo() {
        return todo;
    }

    public Member getMember() {
        return member;
    }
}
