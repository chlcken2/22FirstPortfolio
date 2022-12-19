package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    private int type;
    private String oneIntro;
    private String detailIntro;
    private int style;
    private int maxTeamNo;                  //최대 30
    private int maxMercenaryNo;             //최대 10

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();


}
