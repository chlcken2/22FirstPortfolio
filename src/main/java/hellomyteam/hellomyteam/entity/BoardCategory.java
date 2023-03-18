package hellomyteam.hellomyteam.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardCategory {
    FREE_BOARD("자유게시판"),
    NOTICE_BOARD("공지게시판");

    private final String value;
}
