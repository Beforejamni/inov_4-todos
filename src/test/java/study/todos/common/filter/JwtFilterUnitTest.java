package study.todos.common.filter;

import jakarta.servlet.ServletException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import study.todos.common.util.JwtUtil;

import java.io.IOException;

public class JwtFilterUnitTest {
    @Test
    void filterTest() throws ServletException, IOException {
       String secret = "this-is-very-long-secret-key-this-very-long-secret-key";
       JwtUtil jwtUtil = new JwtUtil(secret);
       jwtUtil.init();
        String token = jwtUtil.createAccessToken("tester");

        JwtFilter jwtFilter = new JwtFilter(jwtUtil);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " +token);

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtFilter.doFilter(req, res, chain);

        String username = (String)req.getAttribute("username");

        Assertions.assertThat(username).isEqualTo("tester");

    }
}
