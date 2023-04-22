package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.Comment;
import hellomyteam.hellomyteam.entity.status.BoardAndCommentStatus;
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
    private Long teamMemberInfoId;
    private Integer likeCount;
    private BoardAndCommentStatus commentStatus;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<CommentResDto> children = new ArrayList<>();
    private String imgUrl;

    public CommentResDto(Long commentId, String content, String writer, Long teamMemberInfoId, Integer likeCount, BoardAndCommentStatus commentStatus, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.commentId = commentId;
        this.content = content;
        this.writer = writer;
        this.teamMemberInfoId = teamMemberInfoId;
        this.likeCount = likeCount;
        this.commentStatus = commentStatus;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static CommentResDto convertCommentToDto(Comment comment) {
        return comment.getCommentStatus() == BoardAndCommentStatus.DELETE_USER ?
                new CommentResDto(comment.getId(), "삭제된 댓글입니다.", "*****", comment.getTeamMemberInfo().getId(), comment.getLikeCount(), comment.getCommentStatus(), comment.getCreatedDate(), comment.getModifiedDate()) :
                new CommentResDto(comment.getId(), comment.getContent(), comment.getWriter(), comment.getTeamMemberInfo().getId(), comment.getLikeCount(), comment.getCommentStatus(), comment.getCreatedDate(), comment.getModifiedDate());
    }
}
