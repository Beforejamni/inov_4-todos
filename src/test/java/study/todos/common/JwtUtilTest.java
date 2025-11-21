package study.todos.common;

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
        String tester = jwtUtil.createJwtToken("tester");
    }

    @Test
    void createJWTRefreshToken() {
        String tester = jwtUtil.createRefreshToken("tester");
    }
}
