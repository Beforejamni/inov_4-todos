package study.todos.domain.todomember.service;

import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.Member.service.SimpleMemberService;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;
import study.todos.domain.todo.service.SimpleTodoService;
import study.todos.domain.todomember.dto.SimpleMemberTodoRes;
import study.todos.domain.todomember.dto.SimpleMembersTodoRes;
import study.todos.domain.todomember.entity.TodoMember;
import study.todos.domain.todomember.repository.JpaTodoMemberRepository;

import java.util.List;
import java.util.Map;

public class SimpleTodoMemberService {
    private final JpaTodoMemberRepository jpaTodoMemberRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final JpaTodoRepository jpaTodoRepository;

    public SimpleTodoMemberService(JpaTodoMemberRepository jpaTodoMemberRepository,
                                   JpaMemberRepository jpaMemberRepository,
                                   JpaTodoRepository jpaTodoRepository) {
        this.jpaTodoMemberRepository = jpaTodoMemberRepository;
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaTodoRepository = jpaTodoRepository;
    }

    public SimpleMemberTodoRes saveMember(Long todoId, Long memberId) {

        Member member = SimpleMemberService.extractMember(jpaMemberRepository, memberId);
        Todo todo = SimpleTodoService.extractTodo(jpaTodoRepository, todoId);

        TodoMember todoMember = new TodoMember(todo, member);
        TodoMember save = jpaTodoMemberRepository.save(todoMember);

        return new SimpleMemberTodoRes( save.getTodo(), save.getMember());
    }

}
