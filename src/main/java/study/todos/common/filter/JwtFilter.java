package study.todos.common.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import study.todos.common.util.JwtUtil;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String token = jwtUtil.extractToken(httpReq);

        if(token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsername(token);

            httpReq.setAttribute("username", username);
        }

        chain.doFilter(request, response);
    }
}
