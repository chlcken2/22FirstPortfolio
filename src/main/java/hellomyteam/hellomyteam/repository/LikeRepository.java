package hellomyteam.hellomyteam.repository;

import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.entity.Comment;
import hellomyteam.hellomyteam.entity.Like;
import hellomyteam.hellomyteam.entity.TeamMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

@Transactional
public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteLikeByTeamMemberInfoAndBoard(TeamMemberInfo teamMemberInfo, Board board);

    void deleteLikeByTeamMemberInfoAndComment(TeamMemberInfo teamMemberInfo, Comment comment);

    Integer countLikeByBoardId(Long boardId);

    Integer countLikeByCommentId(Long commentId);
}
