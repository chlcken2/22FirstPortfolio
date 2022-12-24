package HelloMyTeam.Hellomyteam.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String name;
    private String birthday;    // TODO-sj : 생년월일x -> builder 생성
    @Enumerated(EnumType.STRING) // TODO-sj : 논의
    private MemberStatus memberStatus;

    @Getter
    @AllArgsConstructor
    public enum MemberStatus {
        NORMAL("정상", 0),
        PAUSE("중지", 1);

        private final String name;
        private final int code;
    }
}
