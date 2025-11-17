package study.todos.domain.todomember.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todomember.entity.TodoMember;

import java.util.List;

@Repository
public interface JpaTodoMemberRepository  extends JpaRepository<TodoMember, Long> {

    List<TodoMember> findTodoMembersByTodo(Todo todo);

    Page<Member> findByTodo(Todo todo, Pageable pageable);
}
