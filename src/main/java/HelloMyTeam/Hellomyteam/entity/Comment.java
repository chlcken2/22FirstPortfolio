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
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @NotNull
    private String writer;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardAndCommentStatus commentStatus;

    private Integer likeCount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment")
    private List<CommentReply> commentReply = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;
}
