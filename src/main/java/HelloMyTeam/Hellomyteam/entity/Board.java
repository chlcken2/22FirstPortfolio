package HelloMyTeam.Hellomyteam.entity;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @NotNull
    private int category;
    @NotNull
    private String title;
    @Lob
    @NotNull
    private String contents;
    @NotNull
    private int boardStatus;
    private int likeNo;

    private int viewNo;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Image> images = new ArrayList<>();
}
