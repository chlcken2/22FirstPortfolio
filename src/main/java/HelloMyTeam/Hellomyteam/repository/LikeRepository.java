package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Like;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

@Transactional
public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteLikeByTeamMemberInfoAndBoard(TeamMemberInfo teamMemberInfo, Board board);

    Integer countLikeByBoardId(Long boardId);
}
