package study.todos.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import study.todos.common.util.JwtUtil;

public class JwtUtilTest {

    private static JwtUtil jwtUtil;

    @BeforeAll
    static void setUpJwtUtil() {
        String secret = "this-is-very-long-secret-key-this-very-long-secret-key";
        jwtUtil = new JwtUtil(secret);
        jwtUtil.init();
    }


    @Test
    void createJWTAccessToken() {
        String accessToken = jwtUtil.createAccessToken("tester");

        Assertions.assertThat(jwtUtil.validateToken(accessToken)).isTrue();
        Assertions.assertThat(jwtUtil.getUsername(accessToken)).isEqualTo("tester");
    }

    @Test
    void createJWTRefreshToken() {
        String refreshToken = jwtUtil.createRefreshToken("tester");

        Assertions.assertThat(jwtUtil.validateToken(refreshToken)).isTrue();
        Assertions.assertThat(jwtUtil.getUsername(refreshToken)).isEqualTo("tester");
    }
}
