package hellomyteam.hellomyteam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity(name = "Image")
@Getter
@NoArgsConstructor
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String imageUrl;

    private String storeFilename;

    private Long teamMemberInfoBackGroundId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;

    //logo
    @Builder
    public Image(String imageUrl, String storeFilename, Team team, TeamMemberInfo teamMemberInfo, Long teamMemberInfoBackGroundId) {
        this.imageUrl = imageUrl;
        this.storeFilename = storeFilename;
        this.team = team;
        this.teamMemberInfo = teamMemberInfo;
        this.teamMemberInfoBackGroundId = teamMemberInfoBackGroundId;
    }
}