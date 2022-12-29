package HelloMyTeam.Hellomyteam.exception;

import HelloMyTeam.Hellomyteam.jwt.Token;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtTokenException extends RuntimeException {
    private String token;

    public JwtTokenException(String token) {
        super(String.format("jwt token fail. - token : %s", token));
        this.token = token;
    }
}
