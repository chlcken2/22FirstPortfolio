package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.dto.QTeamSearchParam;
import HelloMyTeam.Hellomyteam.dto.TeamSearchCond;
import HelloMyTeam.Hellomyteam.dto.TeamSearchParam;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.List;

import static HelloMyTeam.Hellomyteam.entity.QMember.member;
import static HelloMyTeam.Hellomyteam.entity.QTeam.team;
import static HelloMyTeam.Hellomyteam.entity.QTeamMemberInfo.teamMemberInfo;

@Repository
@RequiredArgsConstructor
public class TeamCustomImpl{
    private final JPAQueryFactory queryFactory;

    public List<TeamSearchParam> getInfoBySerialNoOrTeamName(TeamSearchCond condition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(team.teamName.eq(condition.getTeamName()));
        }

        if (condition.getTeamSerialNo() != null) {
            builder.and(team.teamSerialNo.eq(condition.getTeamSerialNo()));
        }



        return queryFactory
                .select(new QTeamSearchParam(team.teamName, team.oneIntro, team.teamSerialNo, member.name, team.MemberCount))
                .from(teamMemberInfo)
                .leftJoin(teamMemberInfo.team, team)
                .leftJoin(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")))
                .where(builder)
                .fetch();
    }
}
