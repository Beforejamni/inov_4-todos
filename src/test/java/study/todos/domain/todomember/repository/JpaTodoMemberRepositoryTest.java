package study.todos.domain.todomember.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;
import study.todos.domain.todomember.entity.TodoMember;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;


//통합테스트
@DataJpaTest
@Transactional
public class JpaTodoMemberRepositoryTest {

    @Autowired
    private JpaTodoMemberRepository jpaTodoMemberRepository;

    @Autowired
    private JpaMemberRepository jpaMemberRepository;

    @Autowired
    private JpaTodoRepository jpaTodoRepository;

    @Test
    @DisplayName("일정_유저_관계_생성")
    void saveTodoMember() {

        Todo todo = new Todo();
        Todo saveTodo = jpaTodoRepository.save(todo);

        Member member = new Member("userName", "email");
        Member saveMember = jpaMemberRepository.save(member);

        TodoMember todoMember = new TodoMember(saveTodo, saveMember);

        TodoMember save = jpaTodoMemberRepository.save(todoMember);

        assertThat(save.getTodoMemberId()).isEqualTo(1L);
        assertThat(save.getTodo()).usingRecursiveComparison().isEqualTo(todo);
        assertThat(save.getMember()).usingRecursiveComparison().isEqualTo(member);
    }

    @Test
    @DisplayName("일정_유저_조회")
    void findMembersByTodo() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Todo todo = new Todo("userName", "title", "content");
        Todo saveTodo = jpaTodoRepository.save(todo);
        List<Member> members = IntStream.range(1, 13)
                .mapToObj(i -> jpaMemberRepository.save(new Member("userName" + i, "email")))
                .toList();
        members.forEach(m -> jpaTodoMemberRepository.save(new TodoMember(saveTodo, m)));

        List<Member> expectMembers = members.subList(0, 10);
        //when
        Page<Member> responseMembers = jpaTodoMemberRepository.findByTodo(saveTodo, pageable);

        //then
        assertThat(responseMembers.getContent()).usingRecursiveComparison().isEqualTo(expectMembers);
        assertThat(responseMembers.getTotalPages()).isEqualTo(2L);
    }
}
