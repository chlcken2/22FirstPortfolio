package HelloMyTeam.Hellomyteam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentReqDto {
    private Long teamMemberInfoId;
    private String content;
}
