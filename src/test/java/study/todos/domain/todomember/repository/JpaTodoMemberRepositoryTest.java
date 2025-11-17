package study.todos.domain.todomember.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todo.repository.JpaTodoRepository;
import study.todos.domain.todomember.entity.TodoMember;

@DataJpaTest
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

        Assertions.assertThat(save.getTodoMemberId()).isEqualTo(1L);
        Assertions.assertThat(save.getTodo()).usingRecursiveComparison().isEqualTo(todo);
        Assertions.assertThat(save.getMember()).usingRecursiveComparison().isEqualTo(member);
    }

    @Test
    @DisplayName("일정_안_유저_조회")
    void findTodoMemberByTodoId() {

    }
}
