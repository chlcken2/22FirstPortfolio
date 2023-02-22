package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.CommentReply;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class CommentResDto {
    private Long commentId;
    private List<CommentReply> commentReplies;
    private String content;
    private String writer;
//    private BoardAndCommentStatus commentStatus;
    private LocalDateTime createdDate;

    @QueryProjection
    public CommentResDto(Long commentId, List<CommentReply> commentReplies, String content, String writer, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.commentReplies = commentReplies;
        this.content = content;
        this.writer = writer;
//        this.commentStatus = commentStatus;
        this.createdDate = createdDate;
    }
}
