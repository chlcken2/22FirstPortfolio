package hellomyteam.hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class TeamSearchDto {
    // 검색조건 결과 반환 데이터
    private Long teamId;
    private String teamName;
    private String oneIntro;
    private Integer teamSerialNo;
    private String name;
    private Integer memberCount;
    private String imageUrl;


    @QueryProjection
    public TeamSearchDto(Long teamId, String teamName, String oneIntro, Integer teamSerialNo, String name, Integer memberCount, String imageUrl) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.oneIntro = oneIntro;
        this.teamSerialNo = teamSerialNo;
        this.name = name;
        this.memberCount = memberCount;
        this.imageUrl = imageUrl;
    }
}
