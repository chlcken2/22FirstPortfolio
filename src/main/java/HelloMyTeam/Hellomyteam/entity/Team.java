package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.team.TacticalStyleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    private String oneIntro;

    private String detailIntro;

    @Enumerated(EnumType.STRING)
    private TacticalStyleStatus tacticalStyleStatus;

    private Integer memberCount;                               //팀원 수

    private int mercenaryCount;                            //용병 수

    private Integer teamSerialNo;                          //팀 고유번호
//    @OneToMany(mappedBy = "team")
//    private List<Member> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Image> images = new ArrayList<>();

}
