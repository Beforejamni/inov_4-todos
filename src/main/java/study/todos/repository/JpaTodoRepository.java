package study.todos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.todos.Todo;

import java.util.Optional;

@Repository
public interface JpaTodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByTodoId(Long id);
}
