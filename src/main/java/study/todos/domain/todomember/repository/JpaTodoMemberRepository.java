package study.todos.domain.todomember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.todos.domain.todomember.entity.TodoMember;
@Repository
public interface JpaTodoMemberRepository  extends JpaRepository<TodoMember, Long> {
}
