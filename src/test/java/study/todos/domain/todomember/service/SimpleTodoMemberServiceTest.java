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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.dto.Pagination;
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
import java.util.stream.IntStream;

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

    @Test
    @DisplayName("일정_유저_전체_조회")
    void findMembers() {
        List<Member>  members = IntStream.range(1, 11).mapToObj(i -> new Member("userName" + i, "email")).toList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Member> memberPage = new PageImpl<>(members, pageable, members.size());
        Pagination pagination = new Pagination(memberPage.getNumber(), memberPage.getSize(), members.size(), memberPage.getTotalPages(), memberPage.getTotalElements());
        BDDMockito.given(todoRepository.findById(anyLong())).willReturn(Optional.ofNullable(todo));
        BDDMockito.given(jpaTodoMemberRepository.findByTodo(any(Todo.class), any(Pageable.class))).willReturn(memberPage);

        SimpleMembersTodoRes response = service.findByTodoId(1L, pageable);

        Assertions.assertThat(response.getMembers()).usingRecursiveComparison().isEqualTo(members);
        Assertions.assertThat(response.getTodoId()).isEqualTo(1L);
        Assertions.assertThat(response.getPagination()).usingRecursiveComparison().isEqualTo(pagination);

        BDDMockito.verify(todoRepository).findById(1L);
        BDDMockito.verify(jpaTodoMemberRepository).findByTodo(todo, pageable);
    }
}
