package HelloMyTeam.Hellomyteam.exception;

import HelloMyTeam.Hellomyteam.jwt.model.JwtExceptionStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtTokenException extends RuntimeException {
    private final int code;
    private final String message;

    public JwtTokenException(String errorTitle) {
        this.code = this.getExceptionCode(errorTitle);
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

    private int getExceptionCode(String errorTitle) {
        int code = 0;
        switch (errorTitle) {
            case "MalformedJwtException" :
                code = JwtExceptionStatus.Malformed.getErrorCode();
                break;
            case "ExpiredJwtException" :
                code = JwtExceptionStatus.Expired.getErrorCode();
                break;
            case "UnsupportedJwtException" :
                code = JwtExceptionStatus.Unsupported.getErrorCode();
                break;
            case "Exception" :
                code = JwtExceptionStatus.Exception.getErrorCode();
                break;
        }
        return code;
    }


}
