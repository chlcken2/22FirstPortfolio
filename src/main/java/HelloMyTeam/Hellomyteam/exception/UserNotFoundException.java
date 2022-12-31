package HelloMyTeam.Hellomyteam.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private String email;
    public UserNotFoundException(String email) {
        super(String.format("not found user - email : %s", email));
        this.email = email;
    }
}
