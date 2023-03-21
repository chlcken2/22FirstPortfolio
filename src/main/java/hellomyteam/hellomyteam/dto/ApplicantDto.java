package hellomyteam.hellomyteam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class ApplicantDto {
    private String name;
    private Long memberId;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime applyDate;

    @QueryProjection
    public ApplicantDto(String name, Long memberId, LocalDateTime applyDate) {
        this.name = name;
        this.memberId = memberId;
        this.applyDate = applyDate;
    }
}
