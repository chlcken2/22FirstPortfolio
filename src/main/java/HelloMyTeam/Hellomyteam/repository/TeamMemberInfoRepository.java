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
            "set tmi.authority = \"TEAM_MEMBER\" " +
            "where tmi.member_id in :memberIds " +
            "and tmi.team_id = :teamId",
    nativeQuery = true)
    void findTeamMemberIdsById(@Param("memberIds") List<Long> memberIds, @Param("teamId") Long teamId);
}
