package HelloMyTeam.Hellomyteam.config;

import HelloMyTeam.Hellomyteam.jwt.JwtAuthFilter;
import HelloMyTeam.Hellomyteam.oauth.CustomOAuth2UserService;
import HelloMyTeam.Hellomyteam.oauth.OAuth2SuccessHandler;
import HelloMyTeam.Hellomyteam.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenProvider tokenProvider;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().antMatchers(
                "/h2-console/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**","/token/**", "/user/**").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .addFilterBefore(new JwtExceptionFilter(), OAuth2LoginAuthenticationFilter.class)
//                .oauth2Login().loginPage("/token/expired")
//                .successHandler(oAuth2SuccessHandler)
//                .userInfoEndpoint().userService(oAuth2UserService);

        http.addFilterBefore(new JwtAuthFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}