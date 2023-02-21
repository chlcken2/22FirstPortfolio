package HelloMyTeam.Hellomyteam.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "likes")
public class Like {
    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Like(TeamMemberInfo teamMemberInfo, Comment comment) {
        this.teamMemberInfo = teamMemberInfo;
        this.comment = comment;
    }

    public Like(TeamMemberInfo teamMemberInfo, Board board) {
        this.teamMemberInfo = teamMemberInfo;
        this.board = board;
    }
}
