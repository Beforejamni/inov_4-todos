package study.todos.domain.todomember.dto;

import study.todos.domain.Member.entity.Member;

import java.util.List;

public class SimpleMembersTodoRes {
    private final Long todoId;
    private final List<Member> members;

    public SimpleMembersTodoRes(Long todoId, List<Member> members) {
        this.todoId = todoId;
        this.members = members;
    }

    public Long getTodoId() {
        return todoId;
    }

    public List<Member> getMembers() {
        return members;
    }
}
