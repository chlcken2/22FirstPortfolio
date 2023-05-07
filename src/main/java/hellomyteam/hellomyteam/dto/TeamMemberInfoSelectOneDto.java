package hellomyteam.hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class TeamMemberInfoSelectOneDto {
    private Long teamMemberInfoId;

    @QueryProjection
    public TeamMemberInfoSelectOneDto(Long teamMemberInfoId) {
        this.teamMemberInfoId = teamMemberInfoId;
    }
}
