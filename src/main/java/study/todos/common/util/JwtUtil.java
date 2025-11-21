package study.todos.common.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public  void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwtToken(String username){
        return Jwts.builder()
                .setHeader(createHeader())
                .claim("username" , username)
                .setSubject(username)
                .setIssuer("profile")
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(createExpiredDate(ACCESS_EXP))
                .compact();
    }

    public  String createRefreshToken(String username) {
        return Jwts.builder()
                .setHeader(createHeader())
                .claim("username", username)
                .setSubject(username)
                .setIssuer("profile")
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(createExpiredDate(REFRESH_EXP))
                .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("type", JWT_TYPE);
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private Date createExpiredDate(long expiredDate) {
        Instant expiryDate = Instant.now().plus(Duration.ofMillis(expiredDate));
        return Date.from(expiryDate);
    }
}
