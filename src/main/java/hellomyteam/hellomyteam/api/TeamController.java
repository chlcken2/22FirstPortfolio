package hellomyteam.hellomyteam.api;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Image;
import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.Team;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import hellomyteam.hellomyteam.dto.CommonResponse;
import hellomyteam.hellomyteam.service.MemberService;
import hellomyteam.hellomyteam.service.TeamService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;

    @ApiOperation(value = "teamMemberInfo_id 가져오기", notes = "teamId와 memberId를 통해 팀 회원 id를 가져온다.")
    @GetMapping("/teams/{teamid}/members/{memberid}")
    public CommonResponse<?> getTeamMemberInfoId(@PathVariable(value = "teamid") Long teamId,
                                                 @PathVariable(value = "memberid") Long memberId) {
        return teamService.getTeamMemberInfoId(teamId, memberId);
    }

    @ApiOperation(value = "팀 생성", notes = "img_size 10MB, 팀 생성: 회원테이블에 member_id가 존재해야한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "팀 생성 성공"),
            @ApiResponse(code = 500, message = "memberId가 존재하지 않거나, 탈퇴한 회원입니다.", response = ErrorResponse.class)
    })
    @PostMapping(value = "/team", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<?> saveTeam(@ModelAttribute(value = "teamInfo") TeamDto teamInfo,
                                      @RequestPart(value = "image") MultipartFile image) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();

        Member member = memberService.findMemberByTeamInfo(teamInfo);
        Team team = teamService.createTeamWithAuthNo(teamInfo);

        teamService.teamMemberInfoSaveAuthLeader(team, member);
        List<Image> savedImage = teamService.saveLogo(image, team.getId());

        hashMap.put("createdTeam", team);
        hashMap.put("teamLogo", savedImage);
        return CommonResponse.createSuccess(hashMap);
    }


    @ApiOperation(value = "(팀 찾기)팀 정보 가져오기", notes = "팀 이름 혹은 팀 고유번호를 입력해야한다. ")
    @GetMapping("/teams/{team-identifier}")
    public CommonResponse<?> findTeam (@RequestParam(value = "team-identifier") String teamIdentifier) {
        boolean isNumeric = teamIdentifier.chars().allMatch(Character::isDigit);

        List<TeamSearchDto> findTeams = null;
        if (isNumeric) {
            int teamSerialNo = Integer.parseInt(teamIdentifier);
            findTeams = teamService.findTeamBySearchCond(null, teamSerialNo);
        } else {
            findTeams = teamService.findTeamBySearchCond(teamIdentifier, null);
        }

        if (findTeams.isEmpty()) {
            return CommonResponse.createSuccess(findTeams, "검색 결과가 없습니다.");
        }
        return CommonResponse.createSuccess(findTeams, "검색 결과 success");
    }


    @ApiOperation(value = "(팀 찾기)팀 가입 신청", notes = "가입할 팀Id와 가입할 회원Id를 입력한다, 가입 신청 후 회원 권한 = WAIT")
    @PostMapping("/teams/{teamid}/join")
    public CommonResponse<?> joinTeam(@PathVariable(value = "teamid") Long teamId,
                                      @RequestParam Long memberId) {
        Member member = memberService.findMemberByTeamMemberId(memberId);
        Team team = teamService.findTeamByTeamMemberId(teamId);
        TeamMemberInfo result = teamService.joinTeamAuthWait(team, member);
        return CommonResponse.createSuccess(result, "success");
    }

    //TODO: (알림)팀원 가입 신정자 정보 가져오기,teamMemberInfoId전달해줘서 팀장일 경우 조회가능하게.
    @ApiOperation(value = "(알림) 알림 페이지 팀 가입 신청자 데이터 가져오기", notes = "알림페이지에 띄어질 정보/ teamMemberInfoId= 회원 번호, teamId = 현재 팀")
    @GetMapping("/teams/{teamid}/team-member/{teammemberinfoid}/notifications")
    public CommonResponse<?> getApplicant(@PathVariable(value = "teamid")Long teamId,
                                          @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId) {
        return teamService.findAppliedTeamMember(teamMemberInfoId, teamId);
    }

    @ApiOperation(value = "(알림)팀원 수락", notes = "팀 가입 신청에 따른 팀원 수락, 수락할 memberId와, 가입할 teamId 입력. 가입 후 teamMemberInfoId 발급")
    @PostMapping("/teams/{teamid}/members/{memberid}/accept")
    public CommonResponse<?> acceptTeamMember(@PathVariable(value = "teamid") Long teamId,
                                              @PathVariable(value = "memberid") Long memberId) {
        return teamService.acceptTeamMemberById(teamId, memberId);
    }

    @ApiOperation(value = "(알림)팀원 수락 거절")
    @PostMapping(value = "/teams/{teamid}/members/{memberid}/reject")
    public CommonResponse<?> rejectMember(@PathVariable(value = "teamid") Long teamId,
                                          @PathVariable(value = "memberid") Long memberId) {
        Long count = teamService.deleteMemberByMemberId(teamId, memberId);
        if (count == 0) {
            return CommonResponse.createSuccess(0, "거절된 팀원이 없으므로 API 결과값이 0 입니다.");
        }
        String stringResult = Long.toString(count);
        String template = "총 %s 명이 수락 거절 되었습니다.";
        String message = String.format(template, stringResult);
        return CommonResponse.createSuccess(count, message);
    }

    @ApiOperation(value = "팀 로고 단일 추가 및 존재시 업데이트", notes = "img_size 10MB")
    @PostMapping(value = "/teams/{teamid}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<?> updateLogo(@PathVariable(value = "teamid") Long teamId,
                                        @RequestPart MultipartFile imgFile) throws IOException {
        List<Image> savedImage = teamService.saveLogo(imgFile, teamId);
        return CommonResponse.createSuccess(savedImage, "팀 로고 등록 success <type:List>");
    }

    @ApiOperation(value = "팀 로고 삭제")
    @DeleteMapping(value = "/teams/{teamid}/logo")
    public CommonResponse<?> deleteLogo(@PathVariable(value = "teamid") Long teamId) {
        List<Image> image = teamService.deleteLogoByTeamId(teamId);
        return CommonResponse.createSuccess(image, "팀 로고 삭제 success");
    }

    @ApiOperation(value = "팀 탈퇴")
    @DeleteMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}")
    public CommonResponse<?> withDrawTeam(@PathVariable(value = "teamid") Long teamId,
                                          @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId) {
        return teamService.withDrawTeamByMemberId(teamId, teamMemberInfoId);
    }

    @ApiOperation(value = "팀원 리스트", notes = "team_id를 통한 팀원 리스트 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "페이지네이션 번호", required = true, dataType = "string", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "들고올 데이터 수(API 사용시 메인홈과 팀원 페이지에 적절한 숫자 넣기", required = true, dataType = "string", paramType = "query", defaultValue = "10"),
    })
    @GetMapping(value = "/teams/{teamid}/members")
    public CommonResponse<?> getTeamMemberInfos(@PathVariable(value = "teamid") Long teamId,
                                                @RequestParam int pageNum,
                                                @RequestParam int pageSize) {
        Page<TeamMemberInfosResDto> teamMemberInfosResDtos = teamService.getTeamMemberInfos(teamId, pageNum, pageSize);
        return CommonResponse.createSuccess(teamMemberInfosResDtos, "팀원 리스트 정보 가져오기 success");
    }

    @ApiOperation(value = "팀원 상세 정보 가져오기")
    @GetMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}")
    public CommonResponse<?> getTeamMemberInfo(@PathVariable(value = "teamid") Long teamId,
                                               @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId) {
        return teamService.getTeamMemberInfo(teamId, teamMemberInfoId);
    }

    @ApiOperation(value = "팀원 상세 정보 수정", notes = "본인일 경우에만 수정 가능")
    @PutMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}")
    public CommonResponse<?> editTeamMemberInfo(@PathVariable(value = "teamid") Long teamId,
                                                @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                                @RequestBody TeamInfoUpdateDto teamInfoUpdateDto
    ) {
        return teamService.editTeamMemberInfo(teamId, teamMemberInfoId, teamInfoUpdateDto);
    }

    //TODO 팀 관리/정보 수정
//    @ApiOperation(value = "팀 관리 / 정보 수정")
//    @PutMapping(value = "/member/{memberId}")
//    public CommonResponse<?> editTeamMemberInfo(@PathVariable(value = "memberId") Long memberId,
//                                                @RequestParam(required = true, value = "teamId") Long teamId,
//                                                @RequestBody TeamInfoUpdateDto teamInfoUpdateDto
//    ) {
//        TeamMemberInfoDto teamMemberInfoDto = teamService.editTeamMemberInfo(teamInfoUpdateDto, memberId, teamId);
//        return CommonResponse.createSuccess(teamMemberInfoDto, "내 정보 수정 success");
//    }

    //TODO 팀원 프로필 등록

}
