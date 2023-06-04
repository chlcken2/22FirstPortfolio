package hellomyteam.hellomyteam.repository;

import hellomyteam.hellomyteam.dto.TeamListDto;
import hellomyteam.hellomyteam.entity.Team;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "select new hellomyteam.hellomyteam.dto.TeamListDto(t.id, t.teamName, t.oneIntro, t.teamSerialNo, m.name, t.memberCount, i.imageUrl, t.location, " +
            "    leader_tmi.authority, " +
            "    member_tmi.authority)" +
            "FROM TeamMemberInfo leader_tmi " +
            "JOIN Team t ON leader_tmi.team.id = t.id " +
            "LEFT JOIN Image i ON t.id = i.team.id " +
            "JOIN Member m ON leader_tmi.member.id = m.id " +
            "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberId " +
            "WHERE " +
            "    leader_tmi.authority = 'LEADER' " +
            "ORDER BY " +
            "    t.id asc",
            countQuery = "SELECT COUNT(t) " +
            "FROM TeamMemberInfo leader_tmi " +
            "JOIN Team t ON leader_tmi.team.id = t.id " +
            "JOIN Member m ON leader_tmi.member.id = m.id " +
            "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberId " +
            "WHERE leader_tmi.authority = 'LEADER'"
    )
    Page<TeamListDto> getTeamListAsc(@Param("pageable") Pageable pageable,
                                     @Param("memberId") long memberId);

    @Query(value = "select new hellomyteam.hellomyteam.dto.TeamListDto(t.id, t.teamName, t.oneIntro, t.teamSerialNo, m.name, t.memberCount, i.imageUrl, t.location, " +
            "    leader_tmi.authority, " +
            "    member_tmi.authority)" +
            "FROM TeamMemberInfo leader_tmi " +
            "JOIN Team t ON leader_tmi.team.id = t.id " +
            "LEFT JOIN Image i ON t.id = i.team.id " +
            "JOIN Member m ON leader_tmi.member.id = m.id " +
            "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberId " +
            "WHERE " +
            "    leader_tmi.authority = 'LEADER' " +
            "ORDER BY " +
            "    t.id desc",
            countQuery = "SELECT COUNT(t) " +
                    "FROM TeamMemberInfo leader_tmi " +
                    "JOIN Team t ON leader_tmi.team.id = t.id " +
                    "JOIN Member m ON leader_tmi.member.id = m.id " +
                    "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberId " +
                    "WHERE leader_tmi.authority = 'LEADER'"
    )
    Page<TeamListDto> getTeamListDesc(@Param("pageable") Pageable pageable,
                                     @Param("memberId") long memberId);

    @Query(value = "select new hellomyteam.hellomyteam.dto.TeamListDto(t.id, t.teamName, t.oneIntro, t.teamSerialNo, m.name, t.memberCount, i.imageUrl, t.location, " +
            "    leader_tmi.authority, " +
            "    member_tmi.authority)" +
            "FROM TeamMemberInfo leader_tmi " +
            "JOIN Team t ON leader_tmi.team.id = t.id " +
            "LEFT JOIN Image i ON t.id = i.team.id " +
            "JOIN Member m ON leader_tmi.member.id = m.id " +
            "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberId " +
            "WHERE " +
            "    leader_tmi.authority = 'LEADER' ",
            countQuery = "SELECT COUNT(t) " +
                    "FROM TeamMemberInfo leader_tmi " +
                    "JOIN Team t ON leader_tmi.team.id = t.id " +
                    "JOIN Member m ON leader_tmi.member.id = m.id " +
                    "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberId " +
                    "WHERE leader_tmi.authority = 'LEADER'"
    )
    Page<TeamListDto> getTeamListDefault(@Param("pageable") Pageable pageable,
                                     @Param("memberId") long memberId);

    @Query(value = "select new hellomyteam.hellomyteam.dto.TeamListDto(t.id, t.teamName, t.oneIntro, t.teamSerialNo, m.name, t.memberCount, i.imageUrl, t.location, " +
            "    leader_tmi.authority, " +
            "    member_tmi.authority)" +
            "FROM TeamMemberInfo leader_tmi " +
            "JOIN Team t ON leader_tmi.team.id = t.id " +
            "LEFT JOIN Image i ON t.id = i.team.id " +
            "JOIN Member m ON leader_tmi.member.id = m.id " +
            "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberIdTN " +
            "WHERE " +
            "    leader_tmi.authority = 'LEADER' AND t.teamName = :teamName ",
            countQuery = "SELECT COUNT(t) " +
                    "FROM TeamMemberInfo leader_tmi " +
                    "JOIN Team t ON leader_tmi.team.id = t.id " +
                    "JOIN Member m ON leader_tmi.member.id = m.id " +
                    "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberIdTN " +
                    "WHERE leader_tmi.authority = 'LEADER' AND t.teamName = :teamName "
    )
    List<TeamListDto> getInfoBySerialNoOrTeamName(@Param("teamName") String teamName,
                                                  @Param("memberIdTN") long memberIdTN);

    @Query(value = "select new hellomyteam.hellomyteam.dto.TeamListDto(t.id, t.teamName, t.oneIntro, t.teamSerialNo, m.name, t.memberCount, i.imageUrl, t.location, " +
            "    leader_tmi.authority, " +
            "    member_tmi.authority)" +
            "FROM TeamMemberInfo leader_tmi " +
            "JOIN Team t ON leader_tmi.team.id = t.id " +
            "LEFT JOIN Image i ON t.id = i.team.id " +
            "JOIN Member m ON leader_tmi.member.id = m.id " +
            "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberIdTSN " +
            "WHERE " +
            "    leader_tmi.authority = 'LEADER' and t.teamSerialNo = :teamSerialNo ",
            countQuery = "SELECT COUNT(t) " +
                    "FROM TeamMemberInfo leader_tmi " +
                    "JOIN Team t ON leader_tmi.team.id = t.id " +
                    "JOIN Member m ON leader_tmi.member.id = m.id " +
                    "LEFT JOIN TeamMemberInfo member_tmi ON member_tmi.team.id = t.id AND member_tmi.member.id = :memberIdTSN " +
                    "WHERE leader_tmi.authority = 'LEADER' and t.teamSerialNo = :teamSerialNo "
    )
    List<TeamListDto> getInfoBySerialNoOrTeamName(@Param("teamSerialNo") long teamSerialNo,
                                                  @Param("memberIdTSN") long memberIdTSN);

}