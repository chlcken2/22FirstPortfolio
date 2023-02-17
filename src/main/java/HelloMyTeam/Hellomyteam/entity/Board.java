package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardCategory boardCategory;

    @NotNull
    private String writer;

    @NotNull
    private String title;

    @Lob
    @NotNull
    private String contents;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardAndCommentStatus boardStatus;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount; //조회수

    private Integer commentCount;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "board") // , TODO 댓글 구현시 orphanRemoval = true  추가
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;

    @Builder
    public Board(Long id, BoardCategory boardCategory, String writer, String title, String contents, BoardAndCommentStatus boardStatus, int viewCount, List<Comment> comments, TeamMemberInfo teamMemberInfo) {
        this.id = id;
        this.boardCategory = boardCategory;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.boardStatus = boardStatus;
        this.viewCount = viewCount;
        this.comments = comments;
        this.teamMemberInfo = teamMemberInfo;
    }
}

