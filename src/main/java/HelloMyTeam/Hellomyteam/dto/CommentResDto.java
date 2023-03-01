package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResDto {
    private Long commentId;
    private String content;
    private String writer;
    private BoardAndCommentStatus commentStatus;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<CommentResDto> children = new ArrayList<>();

    public CommentResDto(Long id, String content, String writer, BoardAndCommentStatus commentStatus, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.commentId = id;
        this.content = content;
        this.writer = writer;
        this.commentStatus = commentStatus;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static CommentResDto convertCommentToDto(Comment comment) {
        return comment.getCommentStatus() == BoardAndCommentStatus.DELETE_USER ?
                new CommentResDto(comment.getId(), "삭제된 댓글입니다.", "*****", comment.getCommentStatus(), comment.getCreatedDate(), comment.getModifiedDate()) :
                new CommentResDto(comment.getId(), comment.getContent(), comment.getWriter(), comment.getCommentStatus(), comment.getCreatedDate(), comment.getModifiedDate());
    }
}
