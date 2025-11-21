package study.todos.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.config.PasswordEncoder;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.service.SimpleMemberService;
import study.todos.domain.auth.dto.SimpleAuthReq;
import study.todos.domain.auth.dto.SimpleAuthRes;
import study.todos.domain.auth.entity.Auth;
import study.todos.domain.auth.exception.AuthErrorCode;
import study.todos.domain.auth.exception.AuthException;
import study.todos.domain.auth.repository.JpaAuthRepository;
import study.todos.domain.todo.service.SimpleTodoService;

@Service
public class SimpleAuthService {
    private final JpaAuthRepository jpaAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpleMemberService simpleMemberService;

    public SimpleAuthService(JpaAuthRepository jpaAuthRepository, PasswordEncoder passwordEncoder, SimpleMemberService simpleMemberService){
        this.jpaAuthRepository = jpaAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.simpleMemberService = simpleMemberService;
    }
    @Transactional
    public SimpleAuthRes signUp(SimpleAuthReq req) {

        if(Boolean.TRUE.equals(jpaAuthRepository.existsAuthsByUsername(req.getUsername()))){
            throw new AuthException(AuthErrorCode.DUPLICATION);
        }

        String encodePassword = passwordEncoder.encode(req.getPassword());
        Auth auth = new Auth(req.getUsername(), encodePassword);
        Auth savedAuth = jpaAuthRepository.save(auth);

        SimpleMemberReq simpleMemberReq = new SimpleMemberReq(req.getMemberName(), req.getEmail());
        simpleMemberService.saveMember(simpleMemberReq);

        return new SimpleAuthRes(savedAuth.getUsername(), "회원 가입 성공");
    }
}
