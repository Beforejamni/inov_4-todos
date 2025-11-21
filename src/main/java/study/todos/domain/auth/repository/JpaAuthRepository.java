package study.todos.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.todos.domain.auth.entity.Auth;

import java.util.Optional;

@Repository
public interface JpaAuthRepository extends JpaRepository<Auth, Long> {
    boolean existsAuthsByUsername(String username);

    Optional<Auth> findByUsername(String username);
}
