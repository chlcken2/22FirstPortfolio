package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.TeamMemberCondition;
import hellomyteam.hellomyteam.entity.TeamMemberPersonalPosition;
import com.querydsl.core.annotations.QueryProjection;
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
//    private List<TeamMemberCondition> conditionStatusList;
//    private List<TeamMemberPersonalPosition> preferPositionList;
    private Integer backNumber;
    private String memberOneIntro;
    private String leftRightFoot;
    private Integer conditionIndicator;
    private Integer drinkingCapacity;
    private String image;
    private String phoneNumberVisibility;
    private String birthdayVisibility;


    @QueryProjection
    public TeamMemberInfoDto(Long teamId, String name, String address, String birthday, Integer backNumber, String memberOneIntro, String leftRightFoot, Integer conditionIndicator, Integer drinkingCapacity, String image, String phoneNumberVisibility, String birthdayVisibility) {
        this.teamId = teamId;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
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
