package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.jwt.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String BEARER_TYPE = "Bearer ";
    long ACCESS_TOKEN_VALIDATiON_SECOND = 3600000L;              // 1시간
    long REFRESH_TOKEN_VALIDATiON_SECOND = 3600000 * 24 * 30L;
    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token generateToken(String uid, String role) {
        Date now = new Date();
        Date accessTokenValidTime = new Date(now.getTime() + ACCESS_TOKEN_VALIDATiON_SECOND);
        Date refreshTokenValidTime = new Date(now.getTime() + REFRESH_TOKEN_VALIDATiON_SECOND);

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        String accessToken  = Jwts.builder()
                                    .setClaims(claims)
                                    .setIssuedAt(now)
                                    .setExpiration(accessTokenValidTime)
                                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                                    .compact();

        //Refresh Token
        String refreshToken =  Jwts.builder()
                                    .setClaims(claims)
                    //                .setClaims(claims)
                                    .setIssuedAt(now)
                                    .setExpiration(refreshTokenValidTime)
                                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())  // 사용할 암호화 알고리즘과
                                    .compact();

        return Token.builder()
                .accessToken(BEARER_TYPE + accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean verifyToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
        return claims.getBody().getExpiration().after(new Date());
    }

    public String getUid(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
}