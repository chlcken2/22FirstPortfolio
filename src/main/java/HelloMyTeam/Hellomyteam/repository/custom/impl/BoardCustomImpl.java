package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import static HelloMyTeam.Hellomyteam.entity.QBoard.board;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardCustomImpl {

    private final JPAQueryFactory queryFactory;

    public List<Board> findBoardByTeamId(Long teamId, BoardCategory boardCategory) {
        return queryFactory.selectFrom(board)
                .where(board.teamMemberInfo.team.id.eq(teamId))
                .where(board.boardStatus.eq(BoardAndCommentStatus.NORMAL))
                .where(board.boardCategory.eq(boardCategory))
                .fetch();
    }
}
