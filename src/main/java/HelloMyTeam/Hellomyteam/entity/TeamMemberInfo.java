package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.PersonalPositionStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.PersonalStyleStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.SpecialBadgeStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "teamMemberInfo")
    private List<Image> images = new ArrayList<>();    //background / profile

}
