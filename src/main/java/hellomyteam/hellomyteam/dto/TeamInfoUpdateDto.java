package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.ConditionStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalPositionStatus;
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
    private Boolean changeBirthdayVisibility;
    private Boolean changePhoneNumberVisibility;
}
