package hellomyteam.hellomyteam.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends RuntimeException {
    private String email;
    public MemberNotFoundException(String email, String msg) {
        super(String.format("not found user - email : %s", email));
        this.email = email;
    }

    public MemberNotFoundException(String msg) {
        super(msg);
    }
}
