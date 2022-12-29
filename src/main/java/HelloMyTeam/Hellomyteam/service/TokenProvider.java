package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.jwt.Token;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer ";
    long ACCESS_TOKEN_VALIDATiON_SECOND = 60 * 60 * 24;              // 1시간
    long REFRESH_TOKEN_VALIDATiON_SECOND = 60 * 60 * 24 * 30;   // 1개월
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

//    public Authentication getAuthentication(String accessToken) {
//        Claims claims = this.parseClaims(accessToken);
//
//        if (claims.get(AUTHORITIES_KEY) == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }


    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getUid(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
}