package HelloMyTeam.Hellomyteam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberParam {
    private Long id;
    private String email;
    private String name;
    private String birthday;
    private String memberStatus;

    @Getter
    @Setter
    public static class memberId {
        private Long id;
    }

}
