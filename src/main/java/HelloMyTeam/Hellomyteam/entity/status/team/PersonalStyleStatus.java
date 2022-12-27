package HelloMyTeam.Hellomyteam.entity.status.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* 팀내 개인 스타일 */
@Getter
@RequiredArgsConstructor
public enum PersonalStyleStatus {
    SHORT_PASS("짧은 패스"),
    LONG_PASS("긴 패스"),
    ONE_TO_PASS("원투패스"),
    ZONE_DEFENCE("지역 수비"),
    PRESS_DEFENCE("압박 수비"),
    POWER_HEADER("파워헤딩"),
    LINE_CONTROL("수비 라인 컨트롤"),
    SUPER_SAVING("슈퍼세이브"),
    SLIDING_TACKLE("슬라이딩 태클"),
    TARGET("타겟형 공격수"),
    OVERLAPPING("오버래핑"),
    POWER_SHOOTER("파워 슈터"),
    LINE_BREAKER("잦은 침투"),
    TRIES_FIRST_TIME_SHOT("반박자 빠른 슛"),
    BOX_TO_BOX("박스 투 박스 미드필더"),
    MAESTRO("중원의 지배자"),
    HOLD_UP_PLAY("등지고 딱딱"),
    INDEFATIGABLE_HEART("지치지않는 심장"),
    BALL_KEEPING("탈압박");

    private final String value;
}
