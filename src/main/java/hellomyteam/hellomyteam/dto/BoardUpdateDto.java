package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class BoardUpdateDto {
    private BoardCategory changeBoardCategory;

    private String changeTitle;

    private String changeContents;
}
