package hellomyteam.hellomyteam.entity;

import hellomyteam.hellomyteam.entity.status.ConditionStatus;
import hellomyteam.hellomyteam.entity.status.team.AuthorityStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalPositionStatus;
import hellomyteam.hellomyteam.entity.status.team.PersonalStyleStatus;
import hellomyteam.hellomyteam.entity.status.team.SpecialBadgeStatus;
import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
//    @Enumerated(EnumType.STRING)
//    private PersonalPositionStatus preferPosition;

    @Enumerated(EnumType.STRING)
    private PersonalStyleStatus preferStyle;

    @Enumerated(EnumType.STRING)
    private SpecialBadgeStatus specialTitleStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date withdrawalDate;

    @Enumerated(EnumType.STRING)
    private ConditionStatus conditionStatus;
//    @Enumerated(EnumType.STRING)
//    private ConditionStatus conditionStatus;

    private Integer backNumber;

    private String memberOneIntro;

    private String address;

    private String leftRightFoot;

    private Integer conditionIndicator;

    private Integer drinkingCapacity;

    private Boolean phoneNumberVisibility;

    private Boolean birthdayVisibility;

    private LocalDateTime joinDate;

    private LocalDateTime applyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne(mappedBy = "teamMemberInfo")
    private TeamMemberCondition teamMemberCondition;

    @OneToOne(mappedBy = "teamMemberInfo")
    private TeamMemberPersonalPosition teamMemberPersonalPosition;

    @OneToOne(mappedBy = "teamMemberInfo")
    private Image image;

    @OneToMany(mappedBy = "teamMemberInfo")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "teamMemberInfo")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "teamMemberInfo", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();

}
