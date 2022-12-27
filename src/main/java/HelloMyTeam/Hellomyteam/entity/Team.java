package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.TeamPersonalityStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @NotNull
    private String teamName;

    @NotNull
    private TeamPersonalityStatus teamPersonality;

    @NotNull
    private String oneIntro;

    @NotNull
    private String detailIntro;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthorityStatus.TacticalStyleStatus strategyStyle;

    private int MemberNo;                               //팀원 수

    private int mercenaryNo;                            //용병 수

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();

    @OneToMany(mappedBy = "image")
    @JoinColumn(name = "image_id")
    private Image image;
}
