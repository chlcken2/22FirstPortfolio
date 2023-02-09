package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.dto.QTeamSearchDto;
import HelloMyTeam.Hellomyteam.dto.TeamSearchCondDto;
import HelloMyTeam.Hellomyteam.dto.TeamSearchDto;
import HelloMyTeam.Hellomyteam.entity.*;
import HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static HelloMyTeam.Hellomyteam.entity.QImage.image;
import static HelloMyTeam.Hellomyteam.entity.QMember.member;
import static HelloMyTeam.Hellomyteam.entity.QTeam.team;
import static HelloMyTeam.Hellomyteam.entity.QTeamMemberInfo.teamMemberInfo;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TeamCustomImpl {
    private final JPAQueryFactory queryFactory;

    public List<TeamSearchDto> getInfoBySerialNoOrTeamName(TeamSearchCondDto condition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(team.teamName.eq(condition.getTeamName()));
        }

        if (condition.getTeamSerialNo() != null) {
            builder.and(team.teamSerialNo.eq(condition.getTeamSerialNo()));
        }

         return queryFactory
        .select(new QTeamSearchDto(
                team.teamName
                , team.oneIntro
                , team.teamSerialNo
                , member.name
                , team.memberCount
                , image.imageUrl
        ))
        .from(teamMemberInfo)
        .join(teamMemberInfo.team, team)
        .join(team.teamLogo, image)
        .join(teamMemberInfo.member, member)
        .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")))
        .where(builder)
         .fetch();
    }


    public List<TeamMemberInfo> findByTeamMember(Team team, Member member) {
        return queryFactory.selectFrom(teamMemberInfo)
                .where(teamMemberInfo.team.eq(team)
                        .and(teamMemberInfo.member.eq(member)))
                .fetch();
    }

    public Long deleteMemberByMemberId(Long teamId, Long memberId) {
        Long count = queryFactory.delete(teamMemberInfo)
                .where(teamMemberInfo.team.id.eq(teamId))
                .where(teamMemberInfo.member.id.eq(memberId))
                .where(teamMemberInfo.authority.eq(AuthorityStatus.WAIT))
                .execute();

        return count;
    }

    public void withDrawTeamByMemberId(Long teamId, Long memberId) {
        Long count = queryFactory
                .update(teamMemberInfo)
                .set(teamMemberInfo.authority, AuthorityStatus.WITHDRAW_FROM_TEAM)
                .set(teamMemberInfo.withdrawalDate, new Date())
                .where(teamMemberInfo.authority.ne(AuthorityStatus.WITHDRAW_FROM_TEAM))
                .where(teamMemberInfo.member.id.eq(memberId))
                .where(teamMemberInfo.team.id.eq(teamId))
                .execute();

        queryFactory
                .update(team)
                .set(team.memberCount, team.memberCount.subtract(count))
                .where(team.id.eq(teamId))
                .execute();
    }

    public AuthorityStatus getTeamMemberAuth(Long teamId, Long memberId) {
        TeamMemberInfo result =  queryFactory
                .selectFrom(teamMemberInfo)
                .where(teamMemberInfo.team.id.eq(teamId))
                .where(teamMemberInfo.member.id.eq(memberId))
                .fetchOne();
        return result.getAuthority();
    }
}
