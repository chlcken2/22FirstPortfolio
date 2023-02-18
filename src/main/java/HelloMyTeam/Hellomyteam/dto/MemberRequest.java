package HelloMyTeam.Hellomyteam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequest {
    private String email;
    private String password;

}