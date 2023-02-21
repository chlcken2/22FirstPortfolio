package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResDto {
    private Long id;
    private String content;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String writer;
    private Long boardId;
    private BoardAndCommentStatus commentStatus;
//    private TeamMemberInfo teamMemberInfo;
}
