package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.TeamCond;
import HelloMyTeam.Hellomyteam.dto.TeamParam;
import HelloMyTeam.Hellomyteam.dto.TeamSearchCond;
import HelloMyTeam.Hellomyteam.dto.TeamSearchParam;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.MemberService;
import HelloMyTeam.Hellomyteam.service.TeamService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    @ApiOperation(value = "create", notes = "팀 생성: memberId가 존재해야한다.")
    @PostMapping("/create")
    public CommonResponse<?> saveTeam(@RequestBody TeamParam.TeamInfo teamInfo) {
        Team team = teamService.create(teamInfo);
        Member member = memberService.findMember(teamInfo);
        teamService.teamMemberInfoSave(team, member);
        return CommonResponse.createSuccess(team);
    }

    @ApiOperation(value = "팀 찾기", notes = "팀 이름 혹은 팀 고유번호 입력, ")
    @PostMapping("/find")
    public CommonResponse<?> joinTeam(@RequestBody TeamSearchCond condition) {
        List<TeamSearchParam> findTeam = teamService.findTeam(condition);
        return CommonResponse.createSuccess(findTeam);
    }
}
