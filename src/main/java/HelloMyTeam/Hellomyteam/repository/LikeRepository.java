package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.Like;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

@Transactional
public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteLikeByTeamMemberInfoAndBoard(TeamMemberInfo teamMemberInfo, Board board);

    void deleteLikeByTeamMemberInfoAndComment(TeamMemberInfo teamMemberInfo, Comment comment);

    Integer countLikeByBoardId(Long boardId);

    Integer countLikeByCommentId(Long commentId);
}
