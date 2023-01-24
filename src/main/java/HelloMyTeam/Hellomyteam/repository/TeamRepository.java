package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByTeamSerialNo(int teamSerialNo);
}
