package HelloMyTeam.Hellomyteam.payload;

import HelloMyTeam.Hellomyteam.entity.status.JoinPurpose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String name;
    private String email;
    private JoinPurpose joinPurpose;
}
