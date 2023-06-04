package hellomyteam.hellomyteam.repository.custom.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.Team;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import hellomyteam.hellomyteam.entity.status.team.AuthorityStatus;
import hellomyteam.hellomyteam.repository.custom.TeamJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import static hellomyteam.hellomyteam.entity.QImage.image;
import static hellomyteam.hellomyteam.entity.QMember.member;
import static hellomyteam.hellomyteam.entity.QTeam.team;
import static hellomyteam.hellomyteam.entity.QTeamMemberInfo.teamMemberInfo;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TeamCustomImpl implements TeamJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public List<TeamSearchDto> getInfoBySerialNoOrTeamName(String teamName, Integer teamSerialNo) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(teamName)) {
            builder.and(team.teamName.contains(teamName));
        }

        if (teamSerialNo != null) {
            builder.and(team.teamSerialNo.eq(teamSerialNo));
        }

        return queryFactory
                .select(new QTeamSearchDto(
                        team.id
                        , team.teamName
                        , team.oneIntro
                        , team.teamSerialNo
                        , member.name
                        , team.memberCount
                        , image.imageUrl
                        , team.location
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
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

    public TeamMemberInfoDto findTeamMemberInfoById(Long teamMemberInfoId) {
        return queryFactory.
                select(new QTeamMemberInfoDto(
                        team.id,
                        member.name,
                        teamMemberInfo.address,
                        member.birthday,
                        teamMemberInfo.conditionStatus,
                        teamMemberInfo.backNumber,
                        teamMemberInfo.memberOneIntro,
                        teamMemberInfo.leftRightFoot,
                        teamMemberInfo.conditionIndicator,
                        teamMemberInfo.drinkingCapacity,
                        image.imageUrl,
                        teamMemberInfo.preferPosition,
                        teamMemberInfo.birthdayVisibility,
                        teamMemberInfo.phoneNumberVisibility))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .join(teamMemberInfo.member, member)
                .leftJoin(teamMemberInfo.image, image)
                .where(teamMemberInfo.id.eq(teamMemberInfoId))
                .fetchOne();
    }

    public List<ApplicantDto> getApplyTeamMember(Long teamId) {
        return queryFactory
                .select(new QApplicantDto(
                        member.name
                        , member.id
                        , teamMemberInfo.applyDate
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.member, member)
                .where(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("WAIT")))
                .where(teamMemberInfo.team.id.eq(teamId))
                .fetch();
    }

    @Override
    public Page<TeamMemberInfosResDto> getTeamMemberInfoById(Long teamId, Pageable pageable) {
        List<TeamMemberInfosResDto> content =  queryFactory.select(new QTeamMemberInfosResDto(
                        teamMemberInfo.id
                        ,teamMemberInfo.authority
                        ,teamMemberInfo.preferPosition
                        ,teamMemberInfo.preferStyle
                        ,teamMemberInfo.specialTitleStatus
                        ,teamMemberInfo.conditionStatus
                        ,teamMemberInfo.backNumber
                        ,teamMemberInfo.memberOneIntro
                        ,teamMemberInfo.address
                        ,teamMemberInfo.leftRightFoot
                        ,teamMemberInfo.conditionIndicator
                        ,teamMemberInfo.drinkingCapacity
                        ,member.name
                        ,member.birthday
                        ,image.imageUrl
                        ,teamMemberInfo.modifiedDate
                ))
                .from(teamMemberInfo)
                .leftJoin(teamMemberInfo.image, image)
                .join(teamMemberInfo.member, member)
                .where(teamMemberInfo.team.id.eq(teamId))
                .where(teamMemberInfo.authority.eq(AuthorityStatus.LEADER)
                        .or(teamMemberInfo.authority.eq(AuthorityStatus.SUB_LEADER))
                        .or(teamMemberInfo.authority.eq(AuthorityStatus.TEAM_MEMBER)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(teamMemberInfo.count())
                .from(teamMemberInfo)
                .join(teamMemberInfo.member, member)
                .where(teamMemberInfo.team.id.eq(teamId))
                .where(teamMemberInfo.authority.eq(AuthorityStatus.LEADER)
                        .or(teamMemberInfo.authority.eq(AuthorityStatus.SUB_LEADER))
                        .or(teamMemberInfo.authority.eq(AuthorityStatus.TEAM_MEMBER)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
//    public List<TeamMemberInfosResDto> getTeamMemberInfoById(Long teamId) {
//        return queryFactory.select(new QTeamMemberInfosResDto(
//                teamMemberInfo.id
//                ,teamMemberInfo.authority
//                ,teamMemberInfo.preferPosition
//                ,teamMemberInfo.preferStyle
//                ,teamMemberInfo.specialTitleStatus
//                ,teamMemberInfo.conditionStatus
//                ,teamMemberInfo.backNumber
//                ,teamMemberInfo.memberOneIntro
//                ,teamMemberInfo.address
//                ,teamMemberInfo.leftRightFoot
//                ,teamMemberInfo.conditionIndicator
//                ,teamMemberInfo.drinkingCapacity
//                ,member.name
//                ,member.birthday
//                ,image.imageUrl
//                ))
//                .from(teamMemberInfo)
//                .leftJoin(teamMemberInfo.image, image)
//                .join(teamMemberInfo.member, member)
//                .where(teamMemberInfo.team.id.eq(teamId))
//                .where(teamMemberInfo.authority.eq(AuthorityStatus.LEADER)
//                        .or(teamMemberInfo.authority.eq(AuthorityStatus.SUB_LEADER))
//                        .or(teamMemberInfo.authority.eq(AuthorityStatus.TEAM_MEMBER)))
//                .fetch();
//    }

    public List<TeamNameIdDto> findTeamMemberInfoIdsByMemberId(Long memberId) {
        return queryFactory.select(new QTeamNameIdDto(
                team.teamName
                , team.id
                , image.imageUrl
                , team.oneIntro
                , team.memberCount
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .where(teamMemberInfo.member.id.eq(memberId))
                .where(teamMemberInfo.authority.eq(AuthorityStatus.LEADER)
                        .or(teamMemberInfo.authority.eq(AuthorityStatus.SUB_LEADER))
                        .or(teamMemberInfo.authority.eq(AuthorityStatus.TEAM_MEMBER)))
                .orderBy(teamMemberInfo.createdDate.asc())
                .fetch();
    }

    public Team findTeamByTeamMemberInfoId(Long teamMemberInfoId) {
        return queryFactory.select(team)
                .from(team, teamMemberInfo)
                .where(team.id.eq(teamMemberInfo.team.id))
                .where(teamMemberInfo.id.eq(teamMemberInfoId))
                .fetchOne();
    }

    public Long getTeamMemberInfoIdByIds(Long teamId, Long memberId) {
        return queryFactory.select(teamMemberInfo.id)
                .from(teamMemberInfo)
                .where(teamMemberInfo.team.id.eq(teamId))
                .where(teamMemberInfo.member.id.eq(memberId))
                .fetchOne();

    }


    public Page<TeamSearchDto> getTeamsInfoByDefault(Pageable pageable) {
        List<TeamSearchDto> content =  queryFactory.select(new QTeamSearchDto(
                        team.id
                        , team.teamName
                        , team.oneIntro
                        , team.teamSerialNo
                        , member.name
                        , team.memberCount
                        , image.imageUrl
                        , team.location
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(teamMemberInfo.count())
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<TeamSearchDto> getTeamsInfoByASC(Pageable pageable) {
        List<TeamSearchDto> content = queryFactory.select(new QTeamSearchDto(
                        team.id
                        , team.teamName
                        , team.oneIntro
                        , team.teamSerialNo
                        , member.name
                        , team.memberCount
                        , image.imageUrl
                        , team.location
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(team.id.asc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(teamMemberInfo.count())
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<TeamSearchDto> getTeamsInfoByDESC(Pageable pageable) {
        List<TeamSearchDto> content = queryFactory.select(new QTeamSearchDto(
                        team.id
                        , team.teamName
                        , team.oneIntro
                        , team.teamSerialNo
                        , member.name
                        , team.memberCount
                        , image.imageUrl
                        , team.location
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(team.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(teamMemberInfo.count())
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<TeamSearchDto> getTeamsInfoBySort(Pageable pageable, String Sort, long memberId){

        OrderSpecifier orderSpecifier = getOrderSpecifier(Sort);

        List<TeamSearchDto> content = queryFactory.select(new QTeamSearchDto(
                        team.id
                        , team.teamName
                        , team.oneIntro
                        , team.teamSerialNo
                        , member.name
                        , team.memberCount
                        , image.imageUrl
                        , team.location
                ))
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(teamMemberInfo.count())
                .from(teamMemberInfo)
                .join(teamMemberInfo.team, team)
                .leftJoin(team.teamLogo, image)
                .join(teamMemberInfo.member, member)
                .on(teamMemberInfo.authority.eq(AuthorityStatus.valueOf("LEADER")));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
    public OrderSpecifier<?> getOrderSpecifier(String Sort){

        switch (Sort){
            case "ASC":
                return team.id.asc();
            default:
                return team.id.desc();
        }
    }
    public List<Long> findCurrentIdByLoginUser(String currentUserEmail) {
        return queryFactory.select(teamMemberInfo.id)
                .from(teamMemberInfo)
                .join(teamMemberInfo.member, member)
                .where(teamMemberInfo.member.email.eq(currentUserEmail))
                .fetch();
    }

    public Long getTeamMemberCount(Long teamId) {
        return queryFactory.select(teamMemberInfo.count())
                .from(teamMemberInfo)
                .where(teamMemberInfo.team.id.eq(teamId))
                .where(teamMemberInfo.authority.in(AuthorityStatus.LEADER, AuthorityStatus.SUB_LEADER, AuthorityStatus.TEAM_MEMBER))
                .fetchFirst();
    }

    public void emissionTeamMemberById(Long teamId, Long emissionId) {
        Long count = queryFactory
                .update(teamMemberInfo)
                .set(teamMemberInfo.authority, AuthorityStatus.EMISSION_MEMBER)
                .set(teamMemberInfo.withdrawalDate, new Date())
                .where(teamMemberInfo.authority.eq(AuthorityStatus.TEAM_MEMBER))
                .where(teamMemberInfo.id.eq(emissionId))
                .where(teamMemberInfo.team.id.eq(teamId))
                .execute();

        queryFactory
                .update(team)
                .set(team.memberCount, team.memberCount.subtract(count))
                .where(team.id.eq(teamId))
                .execute();
    }

    public void updateAuthorityTargetId(Long teamId, Long targetId, AuthorityStatus targetAuthority) {
        queryFactory
                .update(teamMemberInfo)
                .set(teamMemberInfo.authority, targetAuthority)
                .where(teamMemberInfo.authority.in(AuthorityStatus.SUB_LEADER, AuthorityStatus.TEAM_MEMBER))
                .where(teamMemberInfo.id.eq(targetId))
                .where(teamMemberInfo.team.id.eq(teamId))
                .execute();
        //즉시 반영
        em.clear();
        em.flush();
    }
}
