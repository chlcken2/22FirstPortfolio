package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.config.S3Uploader;
import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.Image;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.entity.status.ConditionStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import HelloMyTeam.Hellomyteam.repository.FileUploadRepository;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.TeamRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.FileUploadCustomImpl;
import HelloMyTeam.Hellomyteam.repository.custom.impl.TeamCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamCustomImpl teamCustomImpl;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadCustomImpl fileUploadCustomImpl;
    private final S3Uploader s3Uploader;

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

        TeamMemberInfo teamMemberInfo = TeamMemberInfo.builder()
                .authority(AuthorityStatus.WAIT)
                .conditionStatus(ConditionStatus.PASSTION)
                .backNumber(0)
                .leftRightFoot("오른발")
                .conditionIndicator(50)
                .drinkingCapacity(1)
                .team(team)
                .member(member)
                .build();
        teamMemberInfoRepository.save(teamMemberInfo);
        return teamMemberInfo;
    }

    public int acceptTeamMemberById(TeamMemberIdsDto teamMemberIdsParam) {
        //수락 전 auth 상태 체크
        Integer result = teamMemberInfoRepository.checkAuthWait(teamMemberIdsParam.getMemberId(), teamMemberIdsParam.getTeamId());
        if (result == 0 || result == null) {
            log.info("@result: " + result);
            return 0;
        }

        teamMemberInfoRepository.updateTeamMemberAuthById(teamMemberIdsParam.getMemberId(), teamMemberIdsParam.getTeamId());

        int countMember = teamMemberInfoRepository.getMemberCountByTeamId(teamMemberIdsParam.getTeamId());
        teamMemberInfoRepository.updateTeamCount(teamMemberIdsParam.getTeamId(), countMember);
        return result;
    }

    public Team findTeamByTeamMemberId(TeamMemberIdDto teamMemberIdParam) {
        Team team = teamRepository.findById(teamMemberIdParam.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));
        return team;
    }

    public List saveLogo(MultipartFile multipartFile, TeamIdDto teamIdParam) throws IOException {
        Team team = teamRepository.findById(teamIdParam.getTeamId())
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

            Boolean result = fileUploadRepository.existsImageByTeamId(teamIdParam.getTeamId());
            //존재=true
            if (result) {
                fileUploadCustomImpl.updateLogoByTeam(teamIdParam.getTeamId(), image.getImageUrl(), image.getStoreFilename());
            } else {
                fileUploadRepository.save(image);
            }
        }

        List<Image> image =  fileUploadRepository.findImageByTeamId(teamIdParam.getTeamId());
        return image;
    }

    public List<Image> deleteLogoByTeamId(TeamIdDto teamIdParam) {
        fileUploadCustomImpl.changeImageByTeamId(teamIdParam.getTeamId());
        List<Image> image =  fileUploadRepository.findImageByTeamId(teamIdParam.getTeamId());
        return image;
    }


    public Long deleteMemberByMemberId(TeamMemberIdDto teamMemberIdParam) {
        Long count = teamCustomImpl.deleteMemberByMemberId(teamMemberIdParam.getTeamId(), teamMemberIdParam.getMemberId());
        return count;
    }

    public Map<String, String> withDrawTeamByMemberId(TeamMemberIdDto teamMemberIdParam) {
        Map<String, String> param = new HashMap<>();
        AuthorityStatus authorityStatus = teamCustomImpl.getTeamMemberAuth(teamMemberIdParam.getTeamId(), teamMemberIdParam.getMemberId());

        if (!(authorityStatus.equals(AuthorityStatus.SUB_LEADER) || authorityStatus.equals(AuthorityStatus.TEAM_MEMBER))) {
            String stringResult = String.valueOf(authorityStatus);
            String template = "%s 의 권한일 경우 팀을 탈퇴 할 수 없습니다. 부팀장, 팀원으로 변경바랍니다.";
            String message = String.format(template, stringResult);
            param.put("message", message);
            param.put("authorityStatus", String.valueOf(authorityStatus));
            return param;
        }
        //팀 탈퇴
        teamCustomImpl.withDrawTeamByMemberId(teamMemberIdParam.getTeamId(), teamMemberIdParam.getMemberId());
        String stringResult = String.valueOf(authorityStatus);
        String template = "현재 권한: %s, 해당 팀을 탈퇴하였습니다.";
        String message = String.format(template, stringResult);
        param.put("message", message);
        param.put("authorityStatus", String.valueOf(authorityStatus));
        return param;
    }

    public TeamMemberInfoDto getTeamMemberInfo(Long memberId, Long teamId) {
        TeamMemberInfoDto teamMemberInfoDto = teamCustomImpl.findTeamMemberInfo(memberId, teamId);
        return teamMemberInfoDto;
    }

    @Transactional
    public TeamMemberInfoDto editTeamMemberInfo(TeamInfoUpdateDto teamInfoUpdateDto, Long memberId, Long teamId) {
        teamCustomImpl.updateTeamMemberInfo(teamInfoUpdateDto, memberId, teamId);

        TeamMemberInfoDto findTeamMemberInfoDto = teamCustomImpl.findTeamMemberInfo(memberId, teamId);
        return findTeamMemberInfoDto;
    }

    public List<ApplicantDto> findAppliedTeamMember(Long teamId) {
        List<ApplicantDto> applicantDto = teamCustomImpl.getApplyTeamMember(teamId);
        return applicantDto;
    }
}