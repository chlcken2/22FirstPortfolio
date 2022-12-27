package HelloMyTeam.Hellomyteam.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;


@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser { // 현재 인증된 사용자의 principald을 컨트롤러에 삽입
}