package hellomyteam.hellomyteam.service;

import hellomyteam.hellomyteam.config.S3Uploader;
import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Image;
import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.Team;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import hellomyteam.hellomyteam.entity.status.ConditionStatus;
import hellomyteam.hellomyteam.entity.status.MemberStatus;
import hellomyteam.hellomyteam.entity.status.team.AuthorityStatus;
import hellomyteam.hellomyteam.repository.FileUploadRepository;
import hellomyteam.hellomyteam.repository.MemberRepository;
import hellomyteam.hellomyteam.repository.TeamMemberInfoRepository;
import hellomyteam.hellomyteam.repository.TeamRepository;
import hellomyteam.hellomyteam.repository.custom.impl.FileUploadCustomImpl;
import hellomyteam.hellomyteam.repository.custom.impl.TeamCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final EntityManager em;
    private final TeamRepository teamRepository;
    private final TeamCustomImpl teamCustomImpl;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadCustomImpl fileUploadCustomImpl;
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;

    public Team createTeamWithAuthNo(TeamDto teamInfo) {
        int authNo = (int)(Math.random() * (9999 - 1000 + 1)) + 1000;
        Team team = Team.builder()
                .teamName(teamInfo.getTeamName())
                .oneIntro(teamInfo.getOneIntro())
                .detailIntro(teamInfo.getDetailIntro())
                .tacticalStyleStatus(teamInfo.getTacticalStyleStatus())
                .memberCount(1)
                .teamSerialNo(authNo)
                .build();
        teamRepository.save(team);
        return team;
    }

    public TeamMemberInfo teamMemberInfoSaveAuthLeader(Team team, Member member) {
        TeamMemberInfo teamMemberInfo = TeamMemberInfo.builder()
                .authority(AuthorityStatus.LEADER)
                .team(team)
                .member(member)
                .build();
        TeamMemberInfo savedteamMemberInfo = teamMemberInfoRepository.save(teamMemberInfo);
        return savedteamMemberInfo;
    }

    public List<TeamSearchDto> findTeamBySearchCond(String teamName, Integer teamSerialNo) {
        List<TeamSearchDto> team = teamCustomImpl.getInfoBySerialNoOrTeamName(teamName, teamSerialNo);
        return team;
    }

    public Team findTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));
        return team;
    }

    public TeamMemberInfo joinTeamAuthWait(Team team, Member member) {
        List<TeamMemberInfo> result = teamCustomImpl.findByTeamMember(team, member);

        if (result.size() > 0) {
            log.info("중복 가입신청 체크..." + String.valueOf(result));
            return null;
        }
        LocalDateTime currentDateTime = LocalDateTime.now();

        TeamMemberInfo teamMemberInfo = TeamMemberInfo.builder()
                .authority(AuthorityStatus.WAIT)
                .conditionStatus(ConditionStatus.PASSTION)
                .backNumber(0)
                .leftRightFoot("오른발")
                .conditionIndicator(50)
                .drinkingCapacity(1)
                .team(team)
                .member(member)
                .applyDate(currentDateTime)
                .build();
        teamMemberInfoRepository.save(teamMemberInfo);
        return teamMemberInfo;
    }

    public CommonResponse<?> acceptTeamMemberById(Long teamId, Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        MemberStatus memberStatus = findMember.get().getMemberStatus();
        if (!memberStatus.equals(MemberStatus.NORMAL)) {
            return CommonResponse.createError(memberStatus, "정상 회원이 아닙니다.");
        }

        TeamMemberInfo findTeamMemberInfo = teamMemberInfoRepository.findByTeamIdAndMemberId(teamId, memberId);
        if (!findTeamMemberInfo.getAuthority().equals(AuthorityStatus.WAIT)) {
            return CommonResponse.createError(findTeamMemberInfo.getAuthority(), "현재 소속팀의 팀원이므로, 가입 신청자가 아닙니다.");
        }

        TeamMemberInfo beforeTeamMemberInfo = em.find(TeamMemberInfo.class, findTeamMemberInfo.getId());
        beforeTeamMemberInfo.setAuthority(AuthorityStatus.TEAM_MEMBER);
        beforeTeamMemberInfo.setJoinDate(LocalDateTime.now());

        Team beforeTeam = em.find(Team.class, teamId);
        beforeTeam.setMemberCount(beforeTeam.getMemberCount() + 1);

        return CommonResponse.createSuccess(beforeTeamMemberInfo.getAuthority(), "팀원으로 반영되었습니다.");
    }

    public Team findTeamByTeamMemberId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));
        return team;
    }

    public List saveLogo(MultipartFile multipartFile, Long teamId) throws IOException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));

        if (!multipartFile.isEmpty()) {
            Map<String, String> storedFileURL = s3Uploader.upload(multipartFile, "teamLogo");
            String fileName = storedFileURL.get("fileName");
            String uploadImageUrl = storedFileURL.get("uploadImageUrl");

            Image image = Image.builder()
                    .team(team)
                    .imageUrl(uploadImageUrl)
                    .storeFilename(fileName)
                    .build();

            Boolean result = fileUploadRepository.existsImageByTeamId(teamId);
            //존재=true
            if (result) {
                fileUploadCustomImpl.updateLogoByTeam(teamId, image.getImageUrl(), image.getStoreFilename());
            } else {
                fileUploadRepository.save(image);
            }
        }

        List<Image> image =  fileUploadRepository.findImageByTeamId(teamId);
        return image;
    }

    public List<Image> deleteLogoByTeamId(Long teamId) {
        fileUploadCustomImpl.changeImageByTeamId(teamId);
        List<Image> image =  fileUploadRepository.findImageByTeamId(teamId);
        return image;
    }


    public Long deleteMemberByMemberId(Long teamId, Long memberId) {
        Long count = teamCustomImpl.deleteMemberByMemberId(teamId, memberId);
        return count;
    }

    public CommonResponse<?> withDrawTeamByMemberId(Long teamId, Long teamMemberInfoId) {
        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findTeamMemberInfoById(teamMemberInfoId);
        if (teamMemberInfo.getTeam().getId() != teamId) {
            return CommonResponse.createError("입력한 teamid와 회원이 가입한 팀 id가 다릅니다.");
        }
        AuthorityStatus authorityStatus = teamCustomImpl.getTeamMemberAuth(teamId, teamMemberInfo.getMember().getId());

        if (!(authorityStatus.equals(AuthorityStatus.SUB_LEADER) || authorityStatus.equals(AuthorityStatus.TEAM_MEMBER))) {
            String stringResult = String.valueOf(authorityStatus);
            String template = "%s 의 권한일 경우 팀을 탈퇴 할 수 없습니다. 팀원으로 변경바랍니다.";
            String message = String.format(template, stringResult);

            return CommonResponse.createError(authorityStatus, message);
        }
        //팀 탈퇴
        teamCustomImpl.withDrawTeamByMemberId(teamId, teamMemberInfo.getMember().getId());
        String stringResult = String.valueOf(authorityStatus);
        String template = "현재 권한: %s, 해당 팀을 탈퇴하였습니다.";
        String message = String.format(template, stringResult);
        return CommonResponse.createError(authorityStatus, message);
    }

    public CommonResponse<?> getTeamMemberInfo(Long teamId, Long teamMemberInfoId) {
        TeamMemberInfoDto teamMemberInfoDto = teamCustomImpl.findTeamMemberInfoById(teamMemberInfoId);
        if (teamMemberInfoDto.getTeamId() != teamId) {
            return CommonResponse.createError("입력한 teamid와 회원이 가입한 팀 id가 다릅니다.");
        }
        return CommonResponse.createSuccess(teamMemberInfoDto, "success");
    }

    public CommonResponse<?> editTeamMemberInfo(Long teamId, Long teamMemberInfoId, TeamInfoUpdateDto teamInfoUpdateDto) {
        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findTeamMemberInfoById(teamMemberInfoId);
        if (teamMemberInfo.getMember() == null) {
            return CommonResponse.createError("존재하지 않는 회원입니다.");
        }

        if (teamMemberInfo.getTeam().getId() != teamId) {
            HashMap<String, Long> hashMap = new HashMap<>();
            hashMap.put("입력한 teamid", teamId);
            return CommonResponse.createError(hashMap, "입력한 teamid와 회원이 가입한 팀 id가 다릅니다.");
        }

        TeamMemberInfo findTeamMemberInfo = em.find(TeamMemberInfo.class, teamMemberInfoId);
        Member findMember = em.find(Member.class, teamMemberInfo.getMember().getId());

        findTeamMemberInfo.setAddress(teamInfoUpdateDto.getChangeAddress());
        findTeamMemberInfo.setConditionStatus(teamInfoUpdateDto.getChangeConditionStatus());
        findTeamMemberInfo.setBackNumber(teamInfoUpdateDto.getChangeBackNumber());
        findTeamMemberInfo.setMemberOneIntro(teamInfoUpdateDto.getChangeMemberOneIntro());
        findTeamMemberInfo.setLeftRightFoot(teamInfoUpdateDto.getChangeLeftRightFoot());
        findTeamMemberInfo.setConditionIndicator(teamInfoUpdateDto.getChangeConditionIndicator());
        findTeamMemberInfo.setDrinkingCapacity(teamInfoUpdateDto.getChangeDrinkingCapacity());
        findTeamMemberInfo.setPreferPosition(teamInfoUpdateDto.getChangePreferPosition());
        findMember.setBirthday(teamInfoUpdateDto.getChangeBirthday());
        findMember.setName(teamInfoUpdateDto.getChangeName());

        TeamMemberInfoDto result = teamCustomImpl.findTeamMemberInfoById(teamMemberInfoId);
        return CommonResponse.createSuccess(result, "수정 되었습니다.");
    }

    public CommonResponse<?> findAppliedTeamMember(Long teamMemberInfoId, Long teamId) {
        Optional<TeamMemberInfo> findTeamMemberInfo = teamMemberInfoRepository.findById(teamMemberInfoId);
        AuthorityStatus teamMemberStatus = findTeamMemberInfo.get().getAuthority();
        Long findTeamId = findTeamMemberInfo.get().getTeam().getId();

        if (findTeamId != teamId) {
            return CommonResponse.createSuccess(teamId, "가입한 팀이 아닙니다.");
        }

        if (teamMemberStatus.equals(AuthorityStatus.LEADER)) {
            List<ApplicantDto> applicantDto = teamCustomImpl.getApplyTeamMember(teamId);
            return CommonResponse.createSuccess(applicantDto, "리더의 경우 보여지는 팀 가입 신청 데이터입니다.");
        }
        return CommonResponse.createSuccess("데이터가 없습니다.");
    }

    public Page<TeamMemberInfosResDto> getTeamMemberInfos(Long teamId, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("created_date").descending());
        Page<TeamMemberInfosResDto> teamMemberInfosResDtos = teamCustomImpl.getTeamMemberInfoById(teamId, pageable);
        return teamMemberInfosResDtos;
    }

    public CommonResponse<?> getTeams(int pageNum, int pageSize, String pageSort) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        if (pageSort.equals("ASC")) {
            Page<TeamSearchDto> teamSearchDtos = teamCustomImpl.getTeamsInfoByASC(pageable);
            return CommonResponse.createSuccess(teamSearchDtos, "팀 오름차순(ASC) 리스트 success");

        } else if (pageSort.equals("DESC")) {
            Page<TeamSearchDto> teamSearchDtos = teamCustomImpl.getTeamsInfoByDESC(pageable);
            return CommonResponse.createSuccess(teamSearchDtos, "팀 내림차순(DESC) 리스트 success");

        } else if (pageSort.equals("SHUFFLE")) {
            Page<TeamSearchDto> teamSearchDtos = teamCustomImpl.getTeamsInfoByDefault(pageable);
            List<TeamSearchDto> content = new ArrayList<>(teamSearchDtos.getContent());

            Collections.shuffle(content);

            int fromIndex = (int) pageable.getOffset();
            int toIndex = Math.min(fromIndex + pageable.getPageSize(), content.size());

            List<TeamSearchDto> subList = content.subList(fromIndex, toIndex);
            return CommonResponse.createSuccess(new PageImpl<>(subList, pageable, content.size()), "팀 랜덤(SHUFFLE) 리스트 success");
        }

        return CommonResponse.createError("정확한 pageSort 값을 입력해주세요.");
    }

    public CommonResponse<?> getTeamList(int pageNum, int pageSize, String pageSort, long memberId){

            Pageable pageable = PageRequest.of(pageNum, pageSize);
        if("ASC".equals(pageSort)){
            Page<TeamListDto> teamListDtos = teamRepository.getTeamListAsc(pageable, memberId);
            return CommonResponse.createSuccess(teamListDtos, "팀 리스트 success");

        } else if ("DESC".equals(pageSort)) {
            Page<TeamListDto> teamListDtos = teamRepository.getTeamListDesc(pageable, memberId);
            return CommonResponse.createSuccess(teamListDtos, "팀 리스트 success");

        } else if ("SHUFFLE".equals(pageSort)) {
            Page<TeamListDto> teamListDtos = teamRepository.getTeamListAsc(pageable, memberId);
            List<TeamListDto> content = new ArrayList<>(teamListDtos.getContent());

            Collections.shuffle(content);

            int fromIndex = (int) pageable.getOffset();
            int toIndex = Math.min(fromIndex + pageable.getPageSize(), content.size());

            List<TeamListDto> subList = content.subList(fromIndex, toIndex);
            return CommonResponse.createSuccess(new PageImpl<>(subList, pageable, content.size()), "팀 랜덤(SHUFFLE) 리스트 success");
        }
        return CommonResponse.createError("정확한 pageSort 값을 입력해주세요.");
    }

    public CommonResponse<?> getTeamMemberInfoId(Long teamId, Long memberId) {
        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (!team.isPresent()) {
            return CommonResponse.createError("가입한 팀이 없습니다. teamId를 확인해주세요.");
        }

        if (!member.isPresent()) {
            return CommonResponse.createError("가입한 회원이 아닙니다. memberId를 확인해주세요.");
        }

        boolean checkResult = teamMemberInfoRepository.existsByTeamIdAndMemberId(teamId, memberId);

        if (!checkResult) {
            return CommonResponse.createError("팀에 가입한 회원이 아닙니다. teamId와 memberId를 확인해주세요.");
        }

        Long teamMemberInfoId = teamCustomImpl.getTeamMemberInfoIdByIds(teamId, memberId);

        return CommonResponse.createSuccess(teamMemberInfoId, "teamMemberInfo_Id 값 success");
    }

    /**
     * 팀 가입 취소
     * teamId 와 memberId 로 teamMemberInfoId 를 삭제함
     * @param teamId
     * @param memberId
     * @return
     */
    public CommonResponse<?> cancelJoinTeam(Long teamId, Long memberId){
        int checkDeleteId = teamMemberInfoRepository.deleteTeamMemberInfoById(teamId, memberId);

        if(checkDeleteId > 0 ){
            return CommonResponse.createSuccess("가입이 취소되었습니다.");
        }else{
            return CommonResponse.createError("가입 취소가 실패하였습니다.");
        }
    }
}