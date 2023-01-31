package HelloMyTeam.Hellomyteam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamSearchCond {
    //검색조건으로 넘어오는 데이터
    @ApiModelProperty(notes = "팀이름", required = true, example = "헬로우마이팀")
    private String teamName;
    @ApiModelProperty(notes = "팀 고유번호 4자리", required = true, example = "0000")
    private Integer teamSerialNo;
}
