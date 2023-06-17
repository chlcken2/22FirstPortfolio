package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.ConditionStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalPositionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoUpdateDto {
    private String changeName;
    private String changeAddress;
    private String changeBirthday;
    private List<ConditionStatus> changeConditionStatus;
    private Integer changeBackNumber;
    private String changeMemberOneIntro;
    private String changeLeftRightFoot;
    private Integer changeConditionIndicator;
    private Integer changeDrinkingCapacity;
    private List<PersonalPositionStatus> changePreferPosition;
    private String changeBirthdayVisibility;
    private String changePhoneNumberVisibility;
}
