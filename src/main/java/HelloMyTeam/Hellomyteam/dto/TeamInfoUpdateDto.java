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
    private String changeName;
    private String changeAddress;
    private String changeBirthday;
    private ConditionStatus changeConditionStatus;
    private Integer changeBackNumber;
    private String changeMemberOneIntro;
    private String changeLeftRightFoot;
    private Integer changeConditionIndicator;
    private Integer changeDrinkingCapacity;
    private PersonalPositionStatus changePreferPosition;
    
}
