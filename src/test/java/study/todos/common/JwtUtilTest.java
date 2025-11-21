package study.todos.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.todos.common.util.JwtUtil;

import java.util.Date;

public class JwtUtilTest {

    private static String secret;
    private static JwtUtil jwtUtil;

    @BeforeAll
    static void setUpJwtUtil() {
        secret = "this-is-very-long-secret-key-this-very-long-secret-key";
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

    @Test
    @DisplayName("만료_완료")
    void ExpiredAccessToken() {
        String expiredAccessToken = Jwts.builder()
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis() - 1000L))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        Assertions.assertThat(jwtUtil.validateToken(expiredAccessToken)).isFalse();
    }
}
