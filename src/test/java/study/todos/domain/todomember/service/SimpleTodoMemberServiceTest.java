package study.todos.domain.todomember.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;
import study.todos.domain.todomember.dto.SimpleMemberTodoRes;
import study.todos.domain.todomember.dto.SimpleMembersTodoRes;
import study.todos.domain.todomember.entity.TodoMember;
import study.todos.domain.todomember.repository.JpaTodoMemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class SimpleTodoMemberServiceTest {

    @Mock
    private JpaTodoMemberRepository jpaTodoMemberRepository;

    @Mock
    private JpaTodoRepository todoRepository;

    @Mock
    private JpaMemberRepository memberRepository;

    @InjectMocks
    private SimpleTodoMemberService service;

    private Todo todo;
    private Member member;

    @BeforeEach
    void init() {
        member = new Member("userName", "email");
        todo = new Todo("userName", "title ", "content");
    }


    @Test
    @DisplayName("일정_유저_저장_성공")
    void saveMember() {
        TodoMember todoMember = new TodoMember(todo, member);
        BDDMockito.given(jpaTodoMemberRepository.save(any(TodoMember.class))).willReturn(todoMember);
        BDDMockito.given(todoRepository.findById(anyLong())).willReturn(Optional.ofNullable(todo));
        BDDMockito.given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
        SimpleMemberTodoRes res = service.saveMember(1L, 1L);

        Assertions.assertThat(res.getTodo()).usingRecursiveComparison().isEqualTo(todo);
        Assertions.assertThat(res.getMember()).usingRecursiveComparison().isEqualTo(member);
    }
}
