package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.status.team.TacticalStyleStatus;
import lombok.*;

public class TeamParam {

    @Getter
    @ToString
    public static class TeamInfo {
        private Long memberId;
        private String teamName;
        private String oneIntro;
        private String detailIntro;
        private TacticalStyleStatus tacticalStyleStatus;
    }
}
