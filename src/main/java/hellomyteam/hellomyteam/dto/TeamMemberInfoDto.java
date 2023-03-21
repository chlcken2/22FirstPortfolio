package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.ConditionStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalPositionStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamMemberInfoDto {
    private Long teamId;
    private String name;
    private String address;
    private String birthday;
    private ConditionStatus conditionStatus;
    private Integer backNumber;
    private String memberOneIntro;
    private String leftRightFoot;
    private Integer conditionIndicator;
    private Integer drinkingCapacity;
    private String image;
    private PersonalPositionStatus preferPosition;

    @QueryProjection
    public TeamMemberInfoDto(Long teamId, String name, String address, String birthday, ConditionStatus conditionStatus, Integer backNumber, String memberOneIntro, String leftRightFoot, Integer conditionIndicator, Integer drinkingCapacity, String image, PersonalPositionStatus preferPosition) {
        this.teamId = teamId;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.conditionStatus = conditionStatus;
        this.backNumber = backNumber;
        this.memberOneIntro = memberOneIntro;
        this.leftRightFoot = leftRightFoot;
        this.conditionIndicator = conditionIndicator;
        this.drinkingCapacity = drinkingCapacity;
        this.image = image;
        this.preferPosition = preferPosition;
    }
}
