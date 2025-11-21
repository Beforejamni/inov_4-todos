package study.todos.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    private static Key key;
    private final String secretKey;
    private static final String JWT_TYPE = "JWT";
    private static final long ACCESS_EXP = 1000L * 60 * 30;
    private static final long REFRESH_EXP = 1000L *60 * 60 * 24 * 7;


    public JwtUtil(@Value("${jwt.secret.key}") String secretKey ) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public static String createAccessToken(String username){
        return createToken(username, ACCESS_EXP);
    }

    public static String createRefreshToken(String username) {
        return createToken(username, REFRESH_EXP);
    }

    public boolean validateToken(String token) {
        try{
            extractClaims(token);
            return true;
        }catch (ExpiredJwtException e) {
            return false;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private static String createToken(String username, long expiredDate) {
        return Jwts.builder().setHeader(createHeader())
                .claim("username", username)
                .setSubject(username)
                .setIssuer("profile")
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(createExpiredDate(expiredDate))
                .compact();
    }


    private  static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("type", JWT_TYPE);
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private static Date createExpiredDate(long expiredDate) {
        Instant expiryDate = Instant.now().plus(Duration.ofMillis(expiredDate));
        return Date.from(expiryDate);
    }

    private static Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
    }

    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }
}
