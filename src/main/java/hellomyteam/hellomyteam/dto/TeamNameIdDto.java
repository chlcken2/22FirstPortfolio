package hellomyteam.hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

/**
 * teamName와 teamId를 가집니다.
 */

@Getter
public class TeamNameIdDto {
    private String teamName;
    private Long teamId;
    private String imageUrl;
    private String oneIntro;
    private Integer memberCount;

    @QueryProjection
    public TeamNameIdDto(String teamName, Long teamId, String imageUrl, String oneIntro, Integer memberCount) {
        this.teamName = teamName;
        this.teamId = teamId;
        this.imageUrl = imageUrl;
        this.oneIntro = oneIntro;
        this.memberCount = memberCount;
    }
}
