package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.TacticalStyleStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TeamSearchParam {
    // 검색조건 결과 반환 데이터
    private String teamName;
    private String oneIntro;
    private Integer teamSerialNo;
    private String name;
    private int memberCount;


    @QueryProjection
    public TeamSearchParam(String teamName, String oneIntro, Integer teamSerialNo, String name, int memberCount) {
        this.teamName = teamName;
        this.oneIntro = oneIntro;
        this.teamSerialNo = teamSerialNo;
        this.name = name;
        this.memberCount = memberCount;
    }
}
