package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity(name = "Image")
@Getter
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String path;
    private Long memberId;
    private Long teamId;
    private Long boardNo;

    @OneToOne(mappedBy = "image")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
