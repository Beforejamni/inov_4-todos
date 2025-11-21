package study.todos.domain.auth.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

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


    private static String secret;
    private static JwtUtil jwtUtil;

    @BeforeAll
    static void setUpJwtUtil() {
        secret = "this-is-very-long-secret-key-this-very-long-secret-key";
        jwtUtil = new JwtUtil(secret);
        jwtUtil.init();
    }

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

        BDDMockito.verify(simpleMemberService, never()).saveMember(simpleMemberReq);

    }

    @Test
    @DisplayName("로그인_성공")
    void signIn_성공() {
        Auth auth = new Auth("nickName", "encodedPassword");
        BDDMockito.given(jpaAuthRepository.findByUsername(any(String.class))).willReturn(Optional.of(auth));
        BDDMockito.given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);

        SimpleSignInReq simpleSignInReq =  new SimpleSignInReq("nickName", "password");
        SimpleTokenDto response = simpleAuthService.signIn(simpleSignInReq);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(jwtUtil.validateToken(response.accessToken())).isTrue();
        Assertions.assertThat(jwtUtil.validateToken(response.refreshToken())).isTrue();

        BDDMockito.verify(jpaAuthRepository).findByUsername(simpleSignInReq.username());
        BDDMockito.verify(passwordEncoder).matches(simpleSignInReq.password(),auth.getPassword());
    }

    @Test
    @DisplayName("로그인_실패_아이디_불일치")
    void signIn_아이디_불일치() {
        BDDMockito.given(jpaAuthRepository.findByUsername(any(String.class))).willReturn(Optional.empty());
        SimpleSignInReq signInReq =  new SimpleSignInReq("nickName", "password");

        AuthException authException = assertThrows(AuthException.class, () -> simpleAuthService.signIn(signInReq));

        Assertions.assertThat(authException.getStatus()).isEqualTo(AuthErrorCode.INCONSISTENCY.getStatus());
        Assertions.assertThat(authException.getMessage()).isEqualTo(AuthErrorCode.INCONSISTENCY.getMessage());

        BDDMockito.verify(jpaAuthRepository).findByUsername(signInReq.username());
        BDDMockito.verify(passwordEncoder, never()).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("로그인_실패_비밀번호_불일치")
    void signIn_비밀번호_불일치() {
        Auth auth = new Auth("nickName", "encodedPassword");
        BDDMockito.given(jpaAuthRepository.findByUsername(any(String.class))).willReturn(Optional.of(auth));
        BDDMockito.given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(false);

        SimpleSignInReq signInReq =  new SimpleSignInReq("nickName", "password");
        AuthException authException = assertThrows(AuthException.class, () -> simpleAuthService.signIn(signInReq));

        Assertions.assertThat(authException.getStatus()).isEqualTo(AuthErrorCode.INCONSISTENCY.getStatus());
        Assertions.assertThat(authException.getMessage()).isEqualTo(AuthErrorCode.INCONSISTENCY.getMessage());

        BDDMockito.verify(jpaAuthRepository).findByUsername(signInReq.username());
        BDDMockito.verify(passwordEncoder).matches(signInReq.password(),auth.getPassword());
    }
}
