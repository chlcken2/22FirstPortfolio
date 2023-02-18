package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConditionStatus {
    INJURY("부상🩼️"),
    REST("휴식중\uD83D\uDCA4"),
    PASSTION("열정\uD83D\uDD25"),
    TRIP("여행중🏝"),
    DISPATCH("파견✈️");


    private final String value;

}
