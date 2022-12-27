package HelloMyTeam.Hellomyteam.entity.status.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorityStatus {
    LEADER("팀장"),
    SUB_LEADER("부팀장"),
    TEAM_MEMBER("팀원"),
    MERCENARY("용병"),
    NORMAL("일반"),
    WITHDRAW_FROM_TEAM("팀 탈퇴자");

    private final String value;

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
}
