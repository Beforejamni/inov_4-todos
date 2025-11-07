package study.todos.domain.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.todos.domain.todo.entity.Todo;


@Repository
public interface JpaTodoRepository extends JpaRepository<Todo, Long> , TodoRepository {

    Page<Todo> findAll(Pageable pageable);
}
