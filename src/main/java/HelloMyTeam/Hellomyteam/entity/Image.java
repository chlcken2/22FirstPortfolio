package HelloMyTeam.Hellomyteam.entity;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;


    @Builder
    public Image(String imageUrl, String storeFilename, Team team) {
        this.imageUrl = imageUrl;
        this.storeFilename = storeFilename;
        this.team = team;
    }
}
