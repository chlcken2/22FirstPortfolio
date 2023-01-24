package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.TeamParam;
import HelloMyTeam.Hellomyteam.dto.TeamSearchCond;
import HelloMyTeam.Hellomyteam.dto.TeamSearchParam;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.TeamRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.TeamCustomImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final TeamCustomImpl teamCustomImpl;

    public Team create(TeamParam.TeamInfo teamInfo) {
        int authNo = (int)(Math.random() * (9999 - 1000 + 1)) + 1000;
        Team team = Team.builder()
                .teamName(teamInfo.getTeamName())
                .oneIntro(teamInfo.getOneIntro())
                .detailIntro(teamInfo.getDetailIntro())
                .tacticalStyleStatus(teamInfo.getTacticalStyleStatus())
                .teamSerialNo(authNo)
                .build();
        teamRepository.save(team);
        return team;
    }

    public void teamMemberInfoSave(Team team, Member member) {
        TeamMemberInfo teamMemberInfo = TeamMemberInfo.builder()
                .authority(AuthorityStatus.LEADER)
                .team(team)
                .member(member)
                .build();
        teamMemberInfoRepository.save(teamMemberInfo);
    }

    public List<TeamSearchParam> findTeam(TeamSearchCond condition) {
        List<TeamSearchParam> team = teamCustomImpl.getInfoBySerialNoOrTeamName(condition);
        return team;
    }
}