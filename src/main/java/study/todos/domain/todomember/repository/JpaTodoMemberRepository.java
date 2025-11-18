package study.todos.domain.todomember.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.todo.entity.Todo;
import study.todos.domain.todomember.entity.TodoMember;

import java.util.List;

@Repository
public interface JpaTodoMemberRepository  extends JpaRepository<TodoMember, Long> {


    @Query(value = "select tm.member from TodoMember tm where tm.todo = :todo",
    countQuery = "select count(tm) from TodoMember tm where  tm.todo = :todo")
    Page<Member> findByTodo(@Param("todo") Todo todo, Pageable pageable);


    Todo todo(Todo todo);
}
