package HelloMyTeam.Hellomyteam.entity.status.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* 팀내 선호 포지션 */
@Getter
@RequiredArgsConstructor
public enum PersonalPositionStatus {
    ST("최전방 공격수"),
    RW("우측 공격수"),
    LW("좌측 공격수"),
    CAM("공격형 미드필더"),
    CM("중앙 미드필더"),
    CDM("수비형 미드필더"),
    RB("우측 수비수"),
    RCB("우측 센터백"),
    LCB("좌측 센터백"),
    LB("좌측 수비수"),
    GK("골키퍼");

    private final String value;


}
