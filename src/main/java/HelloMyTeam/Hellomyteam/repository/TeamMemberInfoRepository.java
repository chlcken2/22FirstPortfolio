package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberInfoRepository extends JpaRepository<TeamMemberInfo, Long> {
}
