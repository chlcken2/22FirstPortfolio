package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus  {
    NORMAL("정상"),
    PAUSE("중지"),
    WITHDRAW("탈퇴"),
    WARNING("경고"),
    EXIT("강퇴"),
    Illegal("불법");

    private final String value;

}
