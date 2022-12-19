package HelloMyTeam.Hellomyteam.entity;

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

    private String teamName;

    private int type;
    private String oneIntro;
    private String detailIntro;
    private int style;
    private int memberCount;                //팀원 수
    private int mercenaryCount;             //용병 수

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

}
