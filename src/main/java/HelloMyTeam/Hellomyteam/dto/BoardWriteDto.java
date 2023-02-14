package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class BoardWriteDto {

    private BoardCategory boardCategory;

    private String title;

    private String contents;

    private BoardAndCommentStatus boardStatus;

    private Long teamMemberInfoId;

}
