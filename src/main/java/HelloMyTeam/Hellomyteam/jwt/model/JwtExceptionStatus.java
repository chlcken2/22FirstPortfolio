package HelloMyTeam.Hellomyteam.jwt.model;

import lombok.Getter;

@Getter
public enum JwtExceptionStatus {
    Malformed("MalformedJwtException", 4000,"잘못된 JWT 서명"),
    Expired("ExpiredJwtException",4010, "만료된 JWT 토큰"),

    Unsupported("UnsupportedJwtException",4020, "지원되지 않는 JWT 토큰"),
    Exception("Exception",4030, "잘못된 JWT 토큰");

    private String errorTitle;
    private int errorCode;
    private String errorMsg;

    JwtExceptionStatus(String errorTitle, int errorCode, String errorMsg) {
        this.errorTitle = errorTitle;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
