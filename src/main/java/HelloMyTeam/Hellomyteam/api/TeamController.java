package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.Image;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.MemberService;
import HelloMyTeam.Hellomyteam.service.TeamService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    @ApiOperation(value = "팀 정보 가져오기", notes = "팀 엔티티에 대한 정보를 가져온다.")
    @GetMapping("/{id}")
    public CommonResponse<?> getTeamInfo(@PathVariable Long id) {
        Team team = teamService.findTeamById(id);
        return CommonResponse.createSuccess(team, "team 정보 전달");
    }

    @ApiOperation(value = "팀 생성", notes = "팀 생성: 회원테이블에 member_id가 존재해야한다.")
    @PostMapping("/create")
    public CommonResponse<?> saveTeam(@RequestBody TeamDto teamInfo) {
        Team team = teamService.createTeamWithAuthNo(teamInfo);
        Member member = memberService.findMemberByTeamInfo(teamInfo);
        teamService.teamMemberInfoSaveAuthLeader(team, member);
        return CommonResponse.createSuccess(team);
    }

    @ApiOperation(value = "팀 찾기", notes = "팀 이름 혹은 팀 고유번호를 입력해야한다. ")
    @GetMapping("/find")
    public CommonResponse<?> findTeam (@RequestParam(value = "teamName", required = false) String teamName,
                                       @RequestParam(value = "teamSerialNo", required = false) Integer teamSerialNo) {
        List<TeamSearchDto> findTeams = teamService.findTeamBySearchCond(teamName, teamSerialNo);

        if (findTeams.isEmpty()) {
            return CommonResponse.createSuccess(findTeams, "검색 결과가 없습니다.");
        }
        return CommonResponse.createSuccess(findTeams);
    }


    @ApiOperation(value = "팀 가입신청", notes = "team_id를 통한 팀 가입신청, 가입 수락 전 권한 = WAIT")
    @PostMapping("/join")
    public CommonResponse<?> joinTeam(@RequestBody TeamMemberIdDto teamMemberIdParam) {
        Member member = memberService.findMemberByTeamMemberId(teamMemberIdParam);
        Team team = teamService.findTeamByTeamMemberId(teamMemberIdParam);
        TeamMemberInfo result = teamService.joinTeamAuthWait(team, member);
        return CommonResponse.createSuccess(result, "null일 경우 중복가입 체크, 리더 본인팀 가입 x");
    }

    @ApiOperation(value = "팀원 수락", notes = "팀 가입 신청에 따른 팀원 수락, 가입할 memberId와, 가입할 teamId 입력")
    @PostMapping("/member/accept")
    public CommonResponse<?> acceptTeamMember(@RequestBody TeamMemberIdsDto teamMemberIdsParam) {
        int changeNo = teamService.acceptTeamMemberById(teamMemberIdsParam);

        String stringResult = Integer.toString(changeNo);
        String template = "총 %s 명이 반영되었습니다.";
        String message = String.format(template, stringResult);
        return CommonResponse.createSuccess(true, message);
    }

    @ApiOperation(value = "팀 로고 단일 추가 및 존재시 업데이트",
            notes = "해당 API는 포스트맨에서 진행할 것, " +
                    "KEY: imgFile, VALUE: 이미지파일 / KEY: teamIdParam, VALUE: {\"teamId\": 숫자})" +
                    "참고 링크: https://smooth-foxtrot-e11.notion.site/swagger-mine-6f0e4ca8ad964f56b0c23d86d6780b98"
    )
    @PostMapping(value = "/logo", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> logoUpdate(@RequestPart TeamIdDto teamIdParam, @RequestPart MultipartFile imgFile) throws IOException {
        List<Image> savedImage = teamService.saveLogo(imgFile, teamIdParam);
        return CommonResponse.createSuccess(savedImage, "팀 로고 등록 success <type:List>");
    }

    @ApiOperation(value = "팀 로고 삭제")
    @PostMapping(value = "/logo/delete")
    public CommonResponse<?> logoDelete(@RequestBody TeamIdDto teamIdParam) {
        List<Image> image = teamService.deleteLogoByTeamId(teamIdParam);
        return CommonResponse.createSuccess(image, "팀 로고 삭제 success");
    }

    @ApiOperation(value = "팀원 수락 거절")
    @PostMapping(value = "/member/reject")
    public CommonResponse<?> memberReject(@RequestBody TeamMemberIdDto teamMemberIdParam) {
        Long count = teamService.deleteMemberByMemberId(teamMemberIdParam);
        if (count == 0) {
            return CommonResponse.createSuccess(0, "선택 된 팀원이 없으므로 API 결과값이 0 입니다.");
        }
        String stringResult = Long.toString(count);
        String template = "총 %s 명이 수락 거절 되었습니다.";
        String message = String.format(template, stringResult);
        return CommonResponse.createSuccess(count, message);
    }

    @ApiOperation(value = "팀 탈퇴")
    @PostMapping(value = "/withDraw")
    public CommonResponse<?> teamWithDraw(@RequestBody TeamMemberIdDto teamMemberIdParam) {
        Map<String, String> param = teamService.withDrawTeamByMemberId(teamMemberIdParam);
        return CommonResponse.createSuccess(param.get("authorityStatus"), param.get("message"));
    }
}
