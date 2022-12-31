package HelloMyTeam.Hellomyteam.jwt;

import HelloMyTeam.Hellomyteam.service.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        try {
            if (StringUtils.hasText(token) && tokenProvider.verifyToken(token)) {
                String email = tokenProvider.getUid(token);
                Authentication auth = getAuthentication(email);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (MalformedJwtException e) {
            response.setStatus(496);
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            response.setStatus(498);
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            response.setStatus(497);
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            response.setStatus(499);
            log.info("JWT 토큰 형식이 잘못되었습니다.");
        }


        filterChain.doFilter(request, response);
    }
}