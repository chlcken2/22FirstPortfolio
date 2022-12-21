package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* 축구팀 전술 스타일 */
@Getter
@RequiredArgsConstructor
public enum TacticalStyleStatus {
    POSSESSION("점유율"),
    GEGENPRESSING("게겐프레싱"),
    TIKI_TAKA("티키타카"),
    COUNTER_ATTACK("선수비 후 역습");

    private final String value;
}
