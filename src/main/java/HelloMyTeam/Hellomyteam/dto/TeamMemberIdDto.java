package HelloMyTeam.Hellomyteam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberIdDto {
    private Long teamId;
    private Long memberId;
}
