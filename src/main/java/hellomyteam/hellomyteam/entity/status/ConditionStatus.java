package hellomyteam.hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConditionStatus {
    INJURY("부상"),
    ON_A_BREAK("휴식중"),
    FATIGUE("피로"),
    ENERGY("활력"),
    ON_THE_MEND("회복중"),
    PASSTION("열정"),
    EXCITED("신남"),
    SADNESS("슬픔"),
    PLEASURE("즐거움"),
    THRILL("설렘"),
    DISPATCH("파견️"),
    BUSINESS_TRIP("출장"),
    INDIVIDUAL_PRACTICE("개인연습"),
    TRIP("여행중"),
    OVERTIME("야근");

    private final String value;

}
