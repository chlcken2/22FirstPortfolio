package hellomyteam.hellomyteam.api;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Image;
import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.Team;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import hellomyteam.hellomyteam.dto.CommonResponse;
import hellomyteam.hellomyteam.entity.status.team.AuthorityStatus;
import hellomyteam.hellomyteam.service.ImageService;
import hellomyteam.hellomyteam.service.MemberService;
import hellomyteam.hellomyteam.service.TeamService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
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
    private final ImageService imageService;

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
        List<Image> savedImage = imageService.saveLogo(image, team.getId());

        hashMap.put("createdTeam", team);
        hashMap.put("teamLogo", savedImage);
        return CommonResponse.createSuccess(hashMap);
    }



    //TODO member_id 값을 수동으로 받고 있음 추후 @authenticationpricipal 을 사용하여 이메일 확인 후 member_id 값 가져오는 방식 고려
    @ApiOperation(value = "(팀 찾기) 팀 리스트 가져오기", notes = "팀 이름 혹은 팀 고유번호를 입력해야한다. ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "페이지네이션 번호", required = true, dataType = "string", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "들고올 데이터 수", required = true, dataType = "string", paramType = "query", defaultValue = "40"),
            @ApiImplicitParam(name = "pageSort", value = "정렬 기준(ASC, DESC, SHUFFLE)", required = true, dataType = "string", paramType = "query", defaultValue = "SHUFFLE"),
            @ApiImplicitParam(name = "memberId", value = "member_id 값", required = true, dataType = "long", paramType = "query", defaultValue = "0")
    })
    @GetMapping("/teams")
    public CommonResponse<?> getTeamList (@RequestParam int pageNum,
                                          @RequestParam int pageSize,
                                          @RequestParam String pageSort,
                                          @RequestParam long memberId
    ) {
        if(memberId == 0){ // memberId가 없을 때를 고려
            return teamService.getTeams(pageNum, pageSize, pageSort);
        }
        return teamService.getTeamList(pageNum, pageSize, pageSort, memberId);
    }

    @ApiOperation(value = "(팀 찾기)검색어를 통해 팀 정보 가져오기", notes = "팀 이름 혹은 팀 고유번호를 입력해야한다. ")
    @GetMapping("/teams/{team-identifier}")
    public CommonResponse<?> findTeam (@RequestParam(value = "team-identifier") String teamIdentifier,
                                        @RequestParam(value = "member_id") long memberId) {
        boolean isNumeric = teamIdentifier.chars().allMatch(Character::isDigit);

        List<TeamListDto> findTeams = null;
        if (isNumeric) {
            int teamSerialNo = Integer.parseInt(teamIdentifier);
            findTeams = teamService.findTeamBySearchCond(null, teamSerialNo, memberId);
        } else {
            findTeams = teamService.findTeamBySearchCond(teamIdentifier, null, memberId);
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

    //TODO 팀 가입 취소
    @ApiOperation(value = "팀 가입 취소", notes = "가입 신청한 팀Id 와 가입할 회원 Id를 입력한다. -> team_member_info_id 삭제")
    @DeleteMapping(value = "/teams/{teamid}/cancel/{memberId}")
    public CommonResponse<?> cancelJoinTeam(@PathVariable(value = "teamid") Long teamId,
                                            @PathVariable(value = "memberId") Long memberId) {
        Member member = memberService.findMemberByTeamMemberId(memberId);
        Team team = teamService.findTeamByTeamMemberId(teamId);

        return teamService.cancelJoinTeam(team.getId(), member.getId());
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

    @ApiOperation(value = "팀원 방출", notes = "팀장/부팀장만 회원 탈퇴 가능, teamMemberInfoID=API를 수행하는 팀장/부팀장 teamMemberInfoId, emissionid=방출할 teamMemberInfoId")
    @DeleteMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/{emissionid}")
    public CommonResponse<?> emissionTeamMember(@PathVariable(value = "teamid") Long teamId,
                                          @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                                @PathVariable(value = "emissionid") Long emissionId) {
        return teamService.emissionTeamByMemberId(teamId, teamMemberInfoId, emissionId);
    }

    @ApiOperation(value = "팀 탈퇴", notes = "부팀장/팀원 탈퇴 가능, 팀원이 팀장 한 명일 경우만 가능")
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

    @ApiOperation(value = "팀원 상세 정보 수정", notes = "본인일 경우에만 수정 가능 <br> " +
                                            "changeBirthdayVisibility 공개/비공개는 'PUBLIC', 'PRIVATE' <br>" +
                                            "changePhoneNumberVisibility 공개/비공개는 'PUBLIC', 'private' 으로 처리")
    @PutMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}")
    public CommonResponse<?> editTeamMemberInfo(@PathVariable(value = "teamid") Long teamId,
                                                @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                                @RequestBody TeamInfoUpdateDto teamInfoUpdateDto
    ) {
        return teamService.editTeamMemberInfo(teamId, teamMemberInfoId, teamInfoUpdateDto);
    }

    /**
     * C,U 팀 로고 추가/수정
     * @param teamId
     * @param imgFile
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "팀 로고 단일 추가 및 존재시 업데이트", notes = "img_size 10MB")
    @PostMapping(value = "/teams/{teamid}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<?> updateLogo(@PathVariable(value = "teamid") Long teamId,
                                        @RequestPart MultipartFile imgFile) throws IOException {
        List<Image> savedImage = imageService.saveLogo(imgFile, teamId);
        return CommonResponse.createSuccess(savedImage, "팀 로고 등록 success <type:List>");
    }

    /**
     * 팀 로고 삭제
     * @param teamId
     * @return
     */
    @ApiOperation(value = "팀 로고 삭제")
    @DeleteMapping(value = "/teams/{teamid}/logo")
    public CommonResponse<?> deleteLogo(@PathVariable(value = "teamid") Long teamId) {
        List<Image> image = imageService.deleteLogoByTeamId(teamId);
        return CommonResponse.createSuccess(image, "팀 로고 삭제 success");
    }

    /**
     * 팀 소속 유저 프로필 이미지 추가/수정
     * @param teamMemberInfoId
     * @param imgFile
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "프로필 이미지 단일 추가 및 존재시 업데이트", notes = "img_size 10MB")
    @PostMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<?> updateProfileImg(@PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                              @RequestPart MultipartFile imgFile) throws IOException {

        return imageService.saveProfile(imgFile, teamMemberInfoId);
    }

    /**
     * 팀 소속 유저 프로필 이미지 조회 teamMemberInfoBackground = false
     * @param teamMemberInfoId
     * @return
     */
    @ApiOperation(value = "프로필 이미지 조회하기")
    @GetMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/profile")
    public CommonResponse<?> getProfileImg(@PathVariable(value = "teammemberinfoid") Long teamMemberInfoId) {
        return imageService.getProfile(teamMemberInfoId);
    }

    /**
     * 팀 소속 유저 백그라운드 이미지 추가/수정
     * @param teamMemberInfoId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "프로필 이미지 단일 삭제")
    @DeleteMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/profile")
    public CommonResponse<?> deleteProfileImg(@PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                                 @PathVariable(value = "teamid") Long teamId) {

        return imageService.deleteProfileImg(teamMemberInfoId, teamId);
    }

    /**
     * 팀 소속 유저 백그라운드 이미지 추가/수정
     * @param teamMemberInfoId
     * @param imgFile
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "백그라운드 이미지 단일 추가 및 존재시 업데이트", notes = "img_size 10MB")
    @PostMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/background", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<?> updateBackgroundImg(@PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                              @RequestPart MultipartFile imgFile) throws IOException {

        return imageService.saveBackgroundImg(imgFile, teamMemberInfoId);
    }

    /**
     * 백그라운드 이미지 조회 teamMemberInfoBackground = true
     * @param teamMemberInfoId
     * @return
     */
    @ApiOperation(value = "백그라운드 이미지 조회하기")
    @GetMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/background")
    public CommonResponse<?> getBackgroundImg(@PathVariable(value = "teammemberinfoid") Long teamMemberInfoId) {
        return imageService.getBackgroundImg(teamMemberInfoId);
    }

    /**
     * 팀 소속 유저 백그라운드 이미지 추가/수정
     * @param teamMemberInfoId
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "백그라운드 이미지 단일 삭제")
    @DeleteMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/background")
    public CommonResponse<?> deleteBackgroundImg(@PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                                 @PathVariable(value = "teamid") Long teamId) {

        return imageService.deleteBackgroundImg(teamMemberInfoId, teamId);
    }


    @ApiOperation(value = "팀원 권한 수정", notes = "팀장: 부팀장 -> 팀원, 팀원 -> 부팀장 || 부팀장: 팀원 -> 부팀장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetId", value = "권한이 수정될 회원 Id"),
            @ApiImplicitParam(name = "authorityStatus", value = "수정 원하는 권한 선택"),
            @ApiImplicitParam(name = "teammemberinfoid", value = "수정을 하는 회원 Id"),
            @ApiImplicitParam(name = "teamid", value = "가입한  teamId"),
    })
    @PutMapping(value = "/teams/{teamid}/team-member/{teammemberinfoid}/authority")
    public CommonResponse<?> editTeamMemberAuth(@PathVariable(value = "teamid") Long teamId,
                                                @PathVariable(value = "teammemberinfoid") Long teamMemberInfoId,
                                                @RequestParam Long targetId,
                                                @RequestParam AuthorityStatus authorityStatus) {
        return teamService.changeAuthorityByTeamByMemberId(teamId, teamMemberInfoId, targetId,  authorityStatus);
    }


}
