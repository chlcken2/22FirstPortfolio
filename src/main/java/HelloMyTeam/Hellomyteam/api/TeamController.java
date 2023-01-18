package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.MemberParam;
import HelloMyTeam.Hellomyteam.dto.TeamParam;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.repository.MemberJpaRepository;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.service.MemberService;
import HelloMyTeam.Hellomyteam.service.TeamService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;
    private final MemberJpaRepository memberJpaRepository;

    @ApiOperation(value = "create", notes = "팀 생성")
    @ApiImplicitParam(name = "test" , value = "Access Token 입력x")
    @PostMapping("/team")
    public CommonResponse<?> saveTeam(@RequestBody TeamParam.TeamInfo teamInfo) {
        Team team = teamService.create(teamInfo);
//        Member member = memberJpaRepository.findById(id);
        teamService.save(team);
        return CommonResponse.createSuccess(team);
    }
}
