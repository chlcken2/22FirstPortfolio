package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.AuthorityStatus;
import HelloMyTeam.Hellomyteam.entity.status.PersonalPositionStatus;
import HelloMyTeam.Hellomyteam.entity.status.PersonalStyleStatus;
import HelloMyTeam.Hellomyteam.entity.status.SpecialBadgeStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
public class TeamMemberInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "teamMemberInfo_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthorityStatus authority;

    @Enumerated(EnumType.STRING)
    private PersonalPositionStatus preferPosition;

    @Enumerated(EnumType.STRING)
    private PersonalStyleStatus preferStyle;

    @Enumerated(EnumType.STRING)
    private SpecialBadgeStatus specialTitleStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date withdrawalDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
