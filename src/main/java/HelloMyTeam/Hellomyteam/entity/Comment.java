package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.CommentStatus;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String contents;
    private CommentStatus commentStatus;
    private int boardNo;
    private int memberId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment")
    private CommentReply commentReply;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
