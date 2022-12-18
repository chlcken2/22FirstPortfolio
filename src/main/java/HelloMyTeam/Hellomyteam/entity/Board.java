package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private int category;
    private String title;
    private String contents;
    private int boardStatus;
    private int likeNo;
    private int viewNo;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
