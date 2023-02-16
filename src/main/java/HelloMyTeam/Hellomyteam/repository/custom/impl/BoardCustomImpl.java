package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.dto.BoardResDto;
import HelloMyTeam.Hellomyteam.dto.QBoardResDto;
import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
import static HelloMyTeam.Hellomyteam.entity.QBoard.board;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardCustomImpl {

    private final JPAQueryFactory queryFactory;


    public Page<BoardResDto> findBoardsByTeamId(Long teamId, BoardCategory boardCategory, Pageable pageable) {
        List<BoardResDto> contents = queryFactory
                .select(new QBoardResDto(
                        board.writer,
                        board.title,
                        board.boardStatus,
                        board.viewNo,
                        board.createdDate,
                        board.commentCount
                )).from(board)
                    .where(board.teamMemberInfo.team.id.eq(teamId))
                    .where(board.boardStatus.eq(BoardAndCommentStatus.NORMAL))
                    .where(board.boardCategory.eq(boardCategory))
                    .orderBy(board.createdDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(board.teamMemberInfo.team.id.eq(teamId))
                .where(board.boardStatus.eq(BoardAndCommentStatus.NORMAL))
                .where(board.boardCategory.eq(boardCategory));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }
}
