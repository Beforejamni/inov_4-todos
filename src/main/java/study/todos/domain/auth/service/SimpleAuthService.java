package study.todos.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.config.PasswordEncoder;
import study.todos.common.util.JwtUtil;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.service.SimpleMemberService;
import study.todos.domain.auth.dto.SimpleAuthReq;
import study.todos.domain.auth.dto.SimpleAuthRes;
import study.todos.domain.auth.dto.SimpleSignInReq;
import study.todos.domain.auth.dto.SimpleTokenDto;
import study.todos.domain.auth.entity.Auth;
import study.todos.domain.auth.exception.AuthErrorCode;
import study.todos.domain.auth.exception.AuthException;
import study.todos.domain.auth.repository.JpaAuthRepository;

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

        if(jpaAuthRepository.existsAuthsByUsername(req.getUsername())){
            throw new AuthException(AuthErrorCode.DUPLICATION);
        }

        String encodePassword = passwordEncoder.encode(req.getPassword());
        Auth auth = new Auth(req.getUsername(), encodePassword);
        Auth savedAuth = jpaAuthRepository.save(auth);

        SimpleMemberReq simpleMemberReq = new SimpleMemberReq(req.getMemberName(), req.getEmail());
        simpleMemberService.saveMember(simpleMemberReq);

        return new SimpleAuthRes(savedAuth.getUsername(), "회원 가입 성공");
    }

    @Transactional
    public SimpleTokenDto signIn(SimpleSignInReq req){

        Auth auth = jpaAuthRepository.findByUsername(req.username())
                .orElseThrow(() -> new AuthException(AuthErrorCode.INCONSISTENCY));

        String encodedPassword = auth.getPassword();
        if(!passwordEncoder.matches(req.password(), encodedPassword)) {
            throw new AuthException(AuthErrorCode.INCONSISTENCY);
        }

        String accessToken = JwtUtil.createAccessToken(auth.getUsername());
        String refreshToken = JwtUtil.createRefreshToken(auth.getUsername());

        return new SimpleTokenDto(accessToken, refreshToken);
    }
}
