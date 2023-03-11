package HelloMyTeam.Hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

/**
 * teamName와 teamId를 가집니다.
 */

@Getter
public class TeamNameIdDto {
    private String teamName;
    private Long teamId;

    @QueryProjection
    public TeamNameIdDto(String teamName, Long teamId) {
        this.teamName = teamName;
        this.teamId = teamId;
    }
}
