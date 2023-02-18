package HelloMyTeam.Hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ApplicantDto {
    private String name;
    private Long userId;

    @QueryProjection
    public ApplicantDto(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }
}
