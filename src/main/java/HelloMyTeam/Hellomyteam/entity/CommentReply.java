package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class CommentReply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_reply_id")
    private Long id;

    @NotNull
    private String contents;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardAndCommentStatus commentReplyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
