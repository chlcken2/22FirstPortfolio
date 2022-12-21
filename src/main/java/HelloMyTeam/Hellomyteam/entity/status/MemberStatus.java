package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus  {
    SUCCESS("정상"),
    STOP("중지"),
    WITHDRAW("탈퇴"),
    WARNING("경고"),
    EXIT("강퇴"),
    Illegal("불법");

    private final String value;

}
