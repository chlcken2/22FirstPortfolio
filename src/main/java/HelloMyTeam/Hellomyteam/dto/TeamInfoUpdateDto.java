package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.status.ConditionStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.PersonalPositionStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.TacticalStyleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoUpdateDto {
    private String address;
    private String birthday;
    private ConditionStatus conditionStatus;
    private Integer backNumber;
    private String memberOneIntro;
    private String leftRightFoot;
    private Integer conditionIndicator;
    private Integer drinkingCapacity;
    private PersonalPositionStatus preferPosition;
    
}
