package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import io.swagger.models.auth.In;
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
    private Integer viewCount; //조회수

//    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer commentCount;

//    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer likeCount;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Like> likes = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Comment> comments;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Board(Long id, BoardCategory boardCategory, String writer, String title, String contents, BoardAndCommentStatus boardStatus, Integer viewCount, Integer commentCount, Integer likeCount, Set<Like> likes, List<Comment> comments, TeamMemberInfo teamMemberInfo, Team team) {
        this.id = id;
        this.boardCategory = boardCategory;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.boardStatus = boardStatus;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.likes = likes;
        this.comments = comments;
        this.teamMemberInfo = teamMemberInfo;
        this.team = team;
    }
}

