package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* 팀원 뱃지 [미리 만들어 둠] */
@Getter
@RequiredArgsConstructor
public enum SpecialBadgeStatus {
    BALL_CONTROL("순두부 터치"),
    BEST_DEFENDER("최고의 수비수"),
    BODY_CONTACT("뛰어난 몸싸움"),
    DASH("가장빠른선수"),
    FREEKICK_MASTER_RIGHT_FOOT("베컴의 오른발"),
    FREEKICK_MASTER_LEFT_FOOT("메시의 왼발"),
    LEADER_SHIP("리더십"),
    GOAL_GETTER("득점기계"),
    PASS_MASTER("패스 마스터"),
    BEST_SAVER("최고의 골키퍼"),
    BOX_TO_BOX("지치지않는 심장"),
    MOM("Man Of the Match");

    private final String value;

}
