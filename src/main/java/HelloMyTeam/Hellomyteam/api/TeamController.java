package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.MemberService;
import HelloMyTeam.Hellomyteam.service.TeamService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    @ApiOperation(value = "팀에 대한 데이터", notes = "Home 화면을 구성하는 팀 정보를 전달한다.")
    @PostMapping("/info")
    public CommonResponse<?> getTeamInfo(@RequestBody TeamIdParam teamIdParam) {
        Team team = teamService.findTeamById(teamIdParam);
        return CommonResponse.createSuccess(team, "team 정보 전달");
    }


    @ApiOperation(value = "팀 생성", notes = "팀 생성: 회원테이블에 member_id가 존재해야한다.")
    @PostMapping("/create")
    public CommonResponse<?> saveTeam(@RequestBody TeamParam teamInfo) {
        Team team = teamService.createTeamWithAuthNo(teamInfo);
        Member member = memberService.findMemberByTeamInfo(teamInfo);
        teamService.teamMemberInfoSaveAuthLeader(team, member);
        return CommonResponse.createSuccess(team);
    }

    @ApiOperation(value = "팀 찾기", notes = "팀 이름 혹은 팀 고유번호를 입력해야한다. ")
    @PostMapping("/find")
    public CommonResponse<?> findTeam (@RequestBody TeamSearchCond condition) {
        List<TeamSearchParam> findTeams = teamService.findTeamBySearchCond(condition);

        if (findTeams.isEmpty()) {
            return CommonResponse.createSuccess(findTeams, "검색 결과가 없습니다.");
        }
        return CommonResponse.createSuccess(findTeams);
    }


    @ApiOperation(value = "팀 가입신청", notes = "team_id를 통한 팀 가입신청, 가입 수락 전 권한 = WAIT")
    @PostMapping("/join")
    public CommonResponse<?> joinTeam(@RequestBody TeamMemberIdParam teamMemberIdParam) {
        Member member = memberService.findMemberByTeamMemberId(teamMemberIdParam);
        Team team = teamService.findTeamByTeamMemberId(teamMemberIdParam);
        Boolean result = teamService.joinTeamAuthWait(team, member);
        return CommonResponse.createSuccess(result, "중복가입 체크, 리더 본인팀 가입 x, 가입신청 성공 = true, 가입 신청 실패 = false");
    }

    @ApiOperation(value = "팀원 수락", notes = "팀 가입 신청에 따른 팀원 수락, 가입할 memberId와, 가입할 teamId 입력")
    @PostMapping("/accept")
    public CommonResponse<?> acceptTeamMember(@RequestBody TeamMemberIdsParam teamMemberIdsParam) {
        int changeNo = teamService.acceptTeamMemberById(teamMemberIdsParam);

        String stringResult = Integer.toString(changeNo);
        String template = "총 %s 명이 반영되었습니다.";
        String message = String.format(template, stringResult);
        return CommonResponse.createSuccess(true, message);
    }
}
