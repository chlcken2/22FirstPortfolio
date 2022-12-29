package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String mobile;
    private String name;
    private String email;
    private LocalDate birthday;
    private MemberStatus memberStatus;
    private Team team;
}
