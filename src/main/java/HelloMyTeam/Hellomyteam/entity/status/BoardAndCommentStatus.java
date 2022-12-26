package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardAndCommentStatus {
    NORMAL("정상"),
    DELETE_USER("유저 삭제"),
    DELETE_ADMIN("관리자 삭제");

    private final String value;
}
