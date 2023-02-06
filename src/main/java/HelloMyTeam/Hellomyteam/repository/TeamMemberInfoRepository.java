package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberInfoRepository extends JpaRepository<TeamMemberInfo, Long> {

    @Query(value =
            "update team_member_info tmi " +
            "set tmi.authority = 'TEAM_MEMBER' " +
            "where tmi.member_id in :memberIds " +
            "and tmi.team_id = :teamId "
            ,
    nativeQuery = true)
    void updateTeamMemberAuthById(@Param("memberIds") List<Long> memberIds, @Param("teamId") Long teamId);


    @Query(value =
            "select count(*) " +
            "from team_member_info tmi " +
            "where tmi.team_id = :teamId"
            ,
    nativeQuery = true)
    int getMemberCountByTeamId(@Param("teamId") Long teamId);

    @Query(value =
            "update team as t " +
            "set t.member_count = :countMember " +
            "where t.team_id = :teamId"
            ,
            nativeQuery = true)
    void updateTeamCount(@Param("teamId") Long teamId, @Param("countMember") int countMember);

    @Query(value =
            "select count(*) from team_member_info tmi " +
            "where tmi.authority = 'WAIT' " +
            "and tmi.team_id = :teamId " +
            "and tmi.member_id in :memberIds "
            ,
            nativeQuery = true)
    Integer checkAuthWait(@Param("memberIds") List<Long> memberIds, @Param("teamId") Long teamId);



}
