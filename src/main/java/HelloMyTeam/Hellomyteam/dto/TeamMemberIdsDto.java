package HelloMyTeam.Hellomyteam.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TeamMemberIdsDto {
    private Long teamId;
    private List<Long> memberId;
}
