package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Like;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static HelloMyTeam.Hellomyteam.entity.QLike.like;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeCustomImpl {

    private final JPAQueryFactory queryFactory;

    public Like existsLikeByIds(TeamMemberInfo teamMemberInfo, Board board) {
        return queryFactory.selectFrom(like)
                .where(like.teamMemberInfo.eq(teamMemberInfo))
                .where(like.board.eq(board))
                .fetchOne();
    }
}
