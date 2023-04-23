package hellomyteam.hellomyteam.service;

import hellomyteam.hellomyteam.jwt.Token;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String BEARER_TYPE = "Bearer ";
    long ACCESS_TOKEN_VALIDATiON_SECOND = 3600000L;             // 1시간
    long REFRESH_TOKEN_VALIDATiON_SECOND = 3600000 * 24 * 30L;
    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        System.out.println("여기 실행됨");
        System.out.println(secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.out.println(secretKey);
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
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);

            return claims.getBody().getExpiration().after(new Date());
        }catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getUid(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public String mailToken(String mailId, int authNumber, long expiration_time){
        System.out.println(mailId+"/"+ authNumber+"/"+ expiration_time);
        System.out.println("여기까지 들어옴");
        System.out.println();
        return Jwts.builder()
                .setSubject(mailId)
                .claim("authNumber", authNumber)
                .setExpiration(new Date(System.currentTimeMillis() + expiration_time))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public int getAuthNumber(String token){
        int AuthNumber = (int) Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().get("authNumber");
        return AuthNumber;
    }
}