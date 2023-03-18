package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.BoardCategory;
import hellomyteam.hellomyteam.entity.status.BoardAndCommentStatus;
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
