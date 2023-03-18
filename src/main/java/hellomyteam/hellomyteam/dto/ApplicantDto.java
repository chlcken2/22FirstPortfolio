package hellomyteam.hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ApplicantDto {
    private String name;
    private Long memberId;

    @QueryProjection
    public ApplicantDto(String name, Long memberId) {
        this.name = name;
        this.memberId = memberId;
    }
}
