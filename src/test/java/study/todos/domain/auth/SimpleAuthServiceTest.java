package study.todos.domain.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import study.todos.common.config.PasswordEncoder;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.service.SimpleMemberService;
import study.todos.domain.auth.dto.SimpleAuthReq;
import study.todos.domain.auth.dto.SimpleAuthRes;
import study.todos.domain.auth.entity.Auth;
import study.todos.domain.auth.exception.AuthErrorCode;
import study.todos.domain.auth.exception.AuthException;
import study.todos.domain.auth.repository.JpaAuthRepository;
import study.todos.domain.auth.service.SimpleAuthService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SimpleAuthServiceTest {

    @Mock
    private JpaAuthRepository jpaAuthRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SimpleMemberService simpleMemberService;

    @InjectMocks
    private SimpleAuthService simpleAuthService;

    private SimpleAuthReq req = new SimpleAuthReq("nickname", "password", "MemberName", "email");

    @Test
    @DisplayName("회원_가입_성공")
    void signUp_성공() {

        Auth savedAuth = new Auth("nickname", "password");

        BDDMockito.given(jpaAuthRepository.existsAuthsByUsername(any(String.class))).willReturn(false);
        BDDMockito.given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword");
        BDDMockito.given(jpaAuthRepository.save(any(Auth.class))).willReturn(savedAuth);

        SimpleAuthRes simpleAuthRes = simpleAuthService.signUp(req);

        Assertions.assertThat(simpleAuthRes.getUsername()).isEqualTo("nickname");
        Assertions.assertThat(simpleAuthRes.getMessage()).isEqualTo("회원 가입 성공");

        SimpleMemberReq simpleMemberReq = new SimpleMemberReq("MemberName", "email");

        BDDMockito.verify(simpleMemberService).saveMember(simpleMemberReq);
        BDDMockito.verify(jpaAuthRepository).existsAuthsByUsername("nickname");
        BDDMockito.verify(passwordEncoder).encode("password");
    }

    @Test
    @DisplayName("회원_가입_중복")
    void signUp_중복_실패() {

        BDDMockito.given(jpaAuthRepository.existsAuthsByUsername(any(String.class))).willReturn(true);

        AuthException authException = assertThrows(AuthException.class, () -> simpleAuthService.signUp(req));

        Assertions.assertThat(authException.getStatus()).isEqualTo(AuthErrorCode.DUPLICATION.getStatus());
        Assertions.assertThat(authException.getMessage()).isEqualTo(AuthErrorCode.DUPLICATION.getMessage());

        SimpleMemberReq simpleMemberReq = new SimpleMemberReq("MemberName", "email");

        BDDMockito.verify(simpleMemberService, Mockito.never()).saveMember(simpleMemberReq);

    }
}
