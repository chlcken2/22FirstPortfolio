package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.CommentReply;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResDto {
    private Long commentId;
    private List<CommentReply> commentReplies;
    private String content;
    private String writer;
    private Comment children;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long parentId;
}
