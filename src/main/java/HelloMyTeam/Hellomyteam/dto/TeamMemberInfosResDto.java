package HelloMyTeam.Hellomyteam.dto;


import HelloMyTeam.Hellomyteam.entity.status.ConditionStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.PersonalPositionStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.PersonalStyleStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.SpecialBadgeStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamMemberInfosResDto {
    private Long teamMemberInfoId;

    private AuthorityStatus authorityStatus;

    private PersonalPositionStatus preferPosition;

    private PersonalStyleStatus preferStyle;

    private SpecialBadgeStatus specialTitleStatus;

    private ConditionStatus conditionStatus;

    private Integer backNumber;

    private String memberOneIntro;

    private String address;

    private String leftRightFoot;

    private Integer conditionIndicator;

    private Integer drinkingCapacity;

    private String memberName;

    private String birthday;

    private String imageUrl;

    @QueryProjection
    public TeamMemberInfosResDto(Long teamMemberInfoId, AuthorityStatus authorityStatus, PersonalPositionStatus preferPosition, PersonalStyleStatus preferStyle, SpecialBadgeStatus specialTitleStatus, ConditionStatus conditionStatus, Integer backNumber, String memberOneIntro, String address, String leftRightFoot, Integer conditionIndicator, Integer drinkingCapacity, String memberName, String birthday, String imageUrl) {
        this.teamMemberInfoId = teamMemberInfoId;
        this.authorityStatus = authorityStatus;
        this.preferPosition = preferPosition;
        this.preferStyle = preferStyle;
        this.specialTitleStatus = specialTitleStatus;
        this.conditionStatus = conditionStatus;
        this.backNumber = backNumber;
        this.memberOneIntro = memberOneIntro;
        this.address = address;
        this.leftRightFoot = leftRightFoot;
        this.conditionIndicator = conditionIndicator;
        this.drinkingCapacity = drinkingCapacity;
        this.memberName = memberName;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
    }
}
