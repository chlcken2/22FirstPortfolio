package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.TeamParam;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.MemberService;
import HelloMyTeam.Hellomyteam.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    @ApiOperation(value = "create", notes = "팀 생성: memberId가 존재해야한다.")
    @PostMapping("/team")
    public CommonResponse<?> saveTeam(@RequestBody TeamParam.TeamInfo teamInfo) {
        Team team = teamService.create(teamInfo);
        Member member = memberService.findById(teamInfo);
        teamService.save(team, member);
        return CommonResponse.createSuccess(team);
    }
}
