package HelloMyTeam.Hellomyteam.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String name;
    private String email;
}
