package study.todos.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.todos.domain.auth.entity.Auth;

@Repository
public interface JpaAuthRepository extends JpaRepository<Auth, Long> {
    Object existsAuthsByUsername(String username);
}
