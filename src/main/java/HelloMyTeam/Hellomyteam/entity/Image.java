package HelloMyTeam.Hellomyteam.entity;

import lombok.Getter;
import javax.persistence.*;

@Entity(name = "Image")
@Getter
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String path;

    @OneToOne(mappedBy = "image")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
