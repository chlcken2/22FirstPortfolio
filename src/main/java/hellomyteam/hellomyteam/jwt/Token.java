package hellomyteam.hellomyteam.jwt;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    private String accessToken;
    private String refreshToken;
//    private Long tokenExpiresIn;
}