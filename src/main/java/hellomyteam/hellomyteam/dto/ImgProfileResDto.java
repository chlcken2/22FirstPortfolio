package hellomyteam.hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImgProfileResDto {
    private Long teamMemberInfoId;
    private String imgUrl;
    private String imgName;
    private LocalDateTime createdDate;
    private Long backgroundId;

    @QueryProjection
    public ImgProfileResDto(Long teamMemberInfoId, String imgUrl, String imgName, LocalDateTime createdDate, Long backgroundId) {
        this.teamMemberInfoId = teamMemberInfoId;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.createdDate = createdDate;
        this.backgroundId = backgroundId;
    }
}
