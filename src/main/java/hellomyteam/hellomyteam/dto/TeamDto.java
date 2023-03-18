package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.team.TacticalStyleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "팀 생성 정보", description = "회원Id, 팀 이름, 한 줄 소개, 상세 소개, 팀 전술 스타일")
public class TeamDto {
    @ApiModelProperty(value = "회원 Id")
    private Long memberId;

    @ApiModelProperty(value = "팀 이름")
    private String teamName;

    @ApiModelProperty(value = "한 줄 소개")
    private String oneIntro;

    @ApiModelProperty(value = "상세 소개")
    private String detailIntro;

    @ApiModelProperty(value = "팀 전술 스타일 , \n" +
            "POSSESSION: 점유율,\n" +
            "GEGENPRESSING: 게겐프레싱,\n" +
            "TIKI_TAKA: 티키타카,\n" +
            "COUNTER_ATTACK: 선수비 후 역습")
    private TacticalStyleStatus tacticalStyleStatus;
}
