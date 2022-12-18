package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;
import javax.persistence.*;

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

    @ManyToMany(mappedBy = "team")
    private OwnTeam ownTeam;

    @OneToOne
    private Image image;
}
