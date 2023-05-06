package hellomyteam.hellomyteam.dto;

import com.querydsl.core.annotations.QueryProjection;
import hellomyteam.hellomyteam.entity.status.team.AuthorityStatus;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TeamListDto {
    private Long teamId;
    private String teamName;
    private String oneIntro;
    private Integer teamSerialNo;
    private String name;
    private Integer memberCount;
    private String imageUrl;
    private String location;
    private AuthorityStatus leader_authority;
    private AuthorityStatus member_authority;

    public TeamListDto(Long teamId, String teamName, String oneIntro, Integer teamSerialNo, String name, Integer memberCount, String imageUrl, String location, AuthorityStatus leader_authority,AuthorityStatus member_authority){
        this.teamId = teamId;
        this.teamName = teamName;
        this.oneIntro = oneIntro;
        this.teamSerialNo = teamSerialNo;
        this.name = name;
        this.memberCount = memberCount;
        this.imageUrl = imageUrl;
        this.location = location;
        this.leader_authority = leader_authority;
        this.member_authority = member_authority;
    }
}
