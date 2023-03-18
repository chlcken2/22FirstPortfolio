package hellomyteam.hellomyteam.exception;

import hellomyteam.hellomyteam.jwt.model.JwtExceptionStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtTokenException extends RuntimeException {
    private final String message;

    public JwtTokenException(String errorTitle) {
        this.message = this.getExceptionMsg(errorTitle);
    }

    private String getExceptionMsg(String errorTitle) {
        String msg;
        switch (errorTitle) {
            case "MalformedJwtException" :
                msg = JwtExceptionStatus.Malformed.getErrorMsg();
                break;
            case "ExpiredJwtException" :
                msg = JwtExceptionStatus.Expired.getErrorMsg();
                break;
            case "UnsupportedJwtException" :
                msg = JwtExceptionStatus.Unsupported.getErrorMsg();
                break;
            case "Exception" :
                msg = JwtExceptionStatus.Exception.getErrorMsg();
                break;
            default:
                msg = "토큰 오류";
        }
        return msg;
    }

}
