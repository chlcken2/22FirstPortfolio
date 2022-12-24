package HelloMyTeam.Hellomyteam.entity;

import lombok.*;

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

    @Builder
    public Member(Long id, String email, String name, String birthday, MemberStatus memberStatus) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.birthday = birthday;
        this.memberStatus = memberStatus;
    }

    @Getter
    @AllArgsConstructor
    public enum MemberStatus {
        NORMAL("정상", 0),
        PAUSE("중지", 1);

        private final String name;
        private final int code;
    }
}
