package hellomyteam.hellomyteam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthNumDto {
    private int authNumber;
    private String authCode;
    private String mailId;

}
