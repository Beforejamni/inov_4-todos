package study.todos.domain.auth.service;

import org.springframework.stereotype.Service;
import study.todos.domain.auth.repository.JpaAuthRepository;

@Service
public class SimpleAuthService {
    private final JpaAuthRepository jpaAuthRepository;

    public SimpleAuthService(JpaAuthRepository jpaAuthRepository){
        this.jpaAuthRepository = jpaAuthRepository;
    }
}
