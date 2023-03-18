package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.JoinPurpose;
import hellomyteam.hellomyteam.entity.status.TermsAndCondStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
    private String birthday;
    private JoinPurpose joinPurpose;
    private TermsAndCondStatus termsOfServiceYn;
    private TermsAndCondStatus privacyYn;

}
