package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.TeamMemberCondition;
import hellomyteam.hellomyteam.entity.TeamMemberPersonalPosition;
import com.querydsl.core.annotations.QueryProjection;
import hellomyteam.hellomyteam.entity.status.ConditionStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalPositionStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalStyleStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TeamMemberInfoDto {
    private Long teamId;
    private String name;
    private String address;
    private String birthday;
    private ConditionStatus conditionStatus;
    private PersonalPositionStatus preferPositionList;
//    private List<TeamMemberCondition> conditionStatusList;
//    private List<TeamMemberPersonalPosition> preferPositionList;
    private Integer backNumber;
    private String memberOneIntro;
    private String leftRightFoot;
    private Integer conditionIndicator;
    private Integer drinkingCapacity;
    private String image;
    private Boolean phoneNumberVisibility;
    private Boolean birthdayVisibility;


    @QueryProjection
    public TeamMemberInfoDto(Long teamId, String name, String address, String birthday, ConditionStatus conditionStatus, PersonalPositionStatus preferPositionList, Integer backNumber, String memberOneIntro, String leftRightFoot, Integer conditionIndicator, Integer drinkingCapacity, String image, Boolean phoneNumberVisibility, Boolean birthdayVisibility) {
        this.teamId = teamId;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.conditionStatus = conditionStatus;
        this.preferPositionList = preferPositionList;
        this.backNumber = backNumber;
        this.memberOneIntro = memberOneIntro;
        this.leftRightFoot = leftRightFoot;
        this.conditionIndicator = conditionIndicator;
        this.drinkingCapacity = drinkingCapacity;
        this.image = image;
        this.phoneNumberVisibility = phoneNumberVisibility;
        this.birthdayVisibility = birthdayVisibility;
    }
}
