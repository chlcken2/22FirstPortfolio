package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.status.team.TacticalStyleStatus;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamParam {
    private Long memberId;
    private String teamName;
    private String oneIntro;
    private String detailIntro;
    private TacticalStyleStatus tacticalStyleStatus;
}
