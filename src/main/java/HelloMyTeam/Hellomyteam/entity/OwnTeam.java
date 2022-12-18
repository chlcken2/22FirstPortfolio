package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class OwnTeam {

    @Id
    @GeneratedValue
    @Column(name = "ownTeam_id")
    private Long id;
    private int authority;                      //0-팀장, 1-부팀장, 2-팀원, 3-용병, 4-일반
    private int preferPosition;                 //0-ST, 1-RW, 2-LW, 3-CAM, 4-CM, 5-CDM, 6-LB, 7-LCB, 8-RB,9-RCB, 10-GK
    private int preferStyle;                    //0-중거리슛, 1-테크니컬 드리블러, 2-긴 패스, 3-플레이메이커, 4-딥-라잉 플레이메이커
    private LocalDateTime withdrawalDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany
    @JoinColumn(name = "team_Id")
    private Team team;
}
