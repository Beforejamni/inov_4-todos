package study.todos.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.todos.common.exception.GlobalExceptionHandler;
import study.todos.domain.auth.dto.SimpleAuthReq;
import study.todos.domain.auth.dto.SimpleAuthRes;
import study.todos.domain.auth.dto.SimpleSignInReq;
import study.todos.domain.auth.dto.SimpleTokenDto;
import study.todos.domain.auth.exception.AuthErrorCode;
import study.todos.domain.auth.exception.AuthException;
import study.todos.domain.auth.service.SimpleAuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SimpleAuthControllerTest {

    @Mock
    private SimpleAuthService simpleAuthService;

    @InjectMocks
    private SimpleAuthController simpleAuthController;

    private final ObjectMapper om = new ObjectMapper();

    private MockMvc mockMvc;

    private SimpleAuthReq req;
    private SimpleTokenDto dto;
    private SimpleSignInReq signInReq;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(simpleAuthController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(om.registerModule(new JavaTimeModule())))
                .build();

        req = new SimpleAuthReq("username", "rawPassword", "nickname", "email");
        dto = new SimpleTokenDto("accessToken", "refreshToken");
        signInReq =  new SimpleSignInReq("username", "password");
    }

    @Test
    @DisplayName("회원가입_성공")
    void signUp_성공() throws Exception {
        SimpleAuthRes simpleAuthRes = new SimpleAuthRes("username", "회원가입성공");
        BDDMockito.given(simpleAuthService.signUp(any(SimpleAuthReq.class))).willReturn(simpleAuthRes);

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.message").value("회원가입성공"));

        BDDMockito.verify(simpleAuthService).signUp(req);
    }

    @Test
    @DisplayName("회원가입_실패")
    void signUp_실패() throws Exception {
        SimpleAuthReq req = new SimpleAuthReq("username", "rawPassword", "nickname", "email");
        BDDMockito.given(simpleAuthService.signUp(any(SimpleAuthReq.class))).willThrow(new AuthException(AuthErrorCode.DUPLICATION));

        mockMvc.perform(post("/auth/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(AuthErrorCode.DUPLICATION.getMessage()));
    }

    @Test
    @DisplayName("로그인_성공")
    void signIn_성공() throws Exception {

        BDDMockito.given(simpleAuthService.signIn(any(SimpleSignInReq.class))).willReturn(dto);

        mockMvc.perform(post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(signInReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(dto.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(dto.refreshToken()));

        BDDMockito.verify(simpleAuthService).signIn(signInReq);
    }

    @Test
    @DisplayName("로그인_실패")
    void signIn_실패() throws Exception {
        BDDMockito.given(simpleAuthService.signIn(any(SimpleSignInReq.class))).willThrow(new AuthException(AuthErrorCode.INCONSISTENCY));

        mockMvc.perform(post("/auth/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(signInReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INCONSISTENCY.getMessage()));

        BDDMockito.verify(simpleAuthService).signIn(signInReq);

    }
}
