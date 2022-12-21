package HelloMyTeam.Hellomyteam.entity.status;

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
    RESIGNER("팀 탈퇴자");

    private final String value;
}
