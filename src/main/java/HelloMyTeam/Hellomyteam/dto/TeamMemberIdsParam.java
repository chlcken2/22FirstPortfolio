package HelloMyTeam.Hellomyteam.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TeamMemberIdsParam {
    private Long teamId;
    private List<Long> memberId;
}
