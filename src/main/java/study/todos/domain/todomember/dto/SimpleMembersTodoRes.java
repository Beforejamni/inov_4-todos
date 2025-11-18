package study.todos.domain.todomember.dto;

import study.todos.common.dto.Pagination;
import study.todos.domain.Member.entity.Member;

import java.util.List;

public class SimpleMembersTodoRes {
    private final Long todoId;
    private final List<Member> members;
    private final Pagination pagination;

    public SimpleMembersTodoRes(Long todoId, List<Member> members, Pagination pagination) {
        this.todoId = todoId;
        this.members = members;
        this.pagination = pagination;
    }

    public Long getTodoId() {
        return todoId;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
