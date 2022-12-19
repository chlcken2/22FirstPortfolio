package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class CommentReply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_reply_id")
    private Long id;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
