package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.config.S3Uploader;
import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.Image;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
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


    public Team createTeamWithAuthNo(TeamParam teamInfo) {
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

    public List<TeamSearchParam> findTeamBySearchCond(TeamSearchCond condition) {
        List<TeamSearchParam> team = teamCustomImpl.getInfoBySerialNoOrTeamName(condition);
        return team;
    }

    public Team findTeamById(TeamIdParam teamIdParam) {
        Team team = teamRepository.findById(teamIdParam.getTeamId())
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
                .team(team)
                .member(member)
                .build();
        teamMemberInfoRepository.save(teamMemberInfo);
        return teamMemberInfo;
    }

    public int acceptTeamMemberById(TeamMemberIdsParam teamMemberIdsParam) {
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

    public Team findTeamByTeamMemberId(TeamMemberIdParam teamMemberIdParam) {
        Team team = teamRepository.findById(teamMemberIdParam.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("teamId가 누락되었습니다."));
        return team;
    }

    public List saveLogo(MultipartFile multipartFile, TeamIdParam teamIdParam) throws IOException {
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

    public List<Image> deleteLogoByTeamId(TeamIdParam teamIdParam) {
        fileUploadCustomImpl.changeImageByTeamId(teamIdParam.getTeamId());
        List<Image> image =  fileUploadRepository.findImageByTeamId(teamIdParam.getTeamId());
        return image;
    }
}