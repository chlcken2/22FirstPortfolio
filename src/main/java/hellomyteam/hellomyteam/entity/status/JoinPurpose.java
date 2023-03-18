package hellomyteam.hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JoinPurpose {
    TEAM_CREATE("팀 생성"),
    TEAM_MANAGEMENT("팀 관리");

    private final String value;
}
