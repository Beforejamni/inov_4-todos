package study.todos.domain.todomember.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todomember.dto.SimpleMembersTodoRes;
import study.todos.domain.todomember.entity.TodoMember;
import study.todos.domain.todomember.repository.JpaTodoMemberRepository;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SimpleTodoMemberServiceTest {

    @Mock
    private JpaTodoMemberRepository jpaTodoMemberRepository;

    @InjectMocks
    private SimpleTodoMemberService service;

    @Test
    @DisplayName("일정_유저_저장_성공")
    void saveMember() {
        Member member = new Member("userName", "email");
        TodoMember todoMember = new TodoMember(new Todo(), member);
        BDDMockito.given(jpaTodoMemberRepository.save(any(TodoMember.class))).willReturn(todoMember);

        SimpleMembersTodoRes res  =  service.saveMember(1L , 1L);

        Assertions.assertThat(res.getTodoId()).isEqualTo(1L);
        Assertions.assertThat(res.getMembers()).usingRecursiveComparison().isEqualTo(member);
    }
}
