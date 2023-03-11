package HelloMyTeam.Hellomyteam.repository.custom;

import HelloMyTeam.Hellomyteam.dto.TeamMemberInfosResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamJpaRepository {
    Page<TeamMemberInfosResDto> getTeamMemberInfoById(Long teamId, Pageable pageable);

}
