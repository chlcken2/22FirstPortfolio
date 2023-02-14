package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class BoardUpdateDto {
    private BoardCategory boardCategory;

    private String title;

    private String contents;
}
