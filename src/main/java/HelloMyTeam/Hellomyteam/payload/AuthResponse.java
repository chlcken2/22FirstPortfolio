package HelloMyTeam.Hellomyteam.payload;

import HelloMyTeam.Hellomyteam.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private Token token;
//    private Token accessToken;
//    public AuthResponse(Token accessToken) {
//        this.accessToken = accessToken;
//    }
}
