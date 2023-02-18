package HelloMyTeam.Hellomyteam.repository.custom.impl;

;
import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.entity.QBoard;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import HelloMyTeam.Hellomyteam.util.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import static HelloMyTeam.Hellomyteam.entity.QBoard.board;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardCustomImpl {

    private final JPAQueryFactory queryFactory;


//    public Page<BoardListResDto> findBoardsByTeamId(Long teamId,
//                                                    BoardCategory boardCategory,
//                                                    BoardSearchReqDto boardSearchReqDto,
//                                                    Pageable pageRequest) {
//        log.info("getAllOrderSpecifiers start");
//        List<OrderSpecifier> orders = getAllOrderSpecifiers(pageRequest);
//        log.info("contents start");
//        List<BoardListResDto> contents = queryFactory
//                .select(new QBoardListResDto(
//                        board.writer
//                        , board.title
//                        , board.boardStatus
//                        , board.viewCount
//                        , board.createdDate
//                        , board.commentCount
//                        , board.likeCount
//                )).from(board)
//                .where(board.teamMemberInfo.team.id.eq(teamId))
//                .where(board.boardStatus.eq(BoardAndCommentStatus.NORMAL))
//                .where(board.boardCategory.eq(boardCategory))
//                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
//                .orderBy(board.createdDate.desc())
//                .offset(pageRequest.getOffset())
//                .limit(pageRequest.getPageSize())
//                .fetch();
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(board.count())
//                .from(board)
//                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
//                .where(board.teamMemberInfo.team.id.eq(teamId))
//                .where(board.boardStatus.eq(BoardAndCommentStatus.NORMAL))
//                .where(board.boardCategory.eq(boardCategory));
//
//        return PageableExecutionUtils.getPage(contents, pageRequest, countQuery::fetchOne);
//    }

    public BoardDetailResDto findBoardById(Long id) {
        return queryFactory.select(new QBoardDetailResDto(
                        board.writer
                        , board.title
                        , board.boardStatus
                        , board.boardCategory
                        , board.contents
                        , board.viewCount
                        , board.likeCount
                        , board.createdDate))
                .from(board)
                .where(board.id.eq(id))
                .where(board.boardStatus.eq(BoardAndCommentStatus.NORMAL))
                .fetchOne();
    }

    public int updateView(Long boardId) {
        return (int) queryFactory.update(board)
                .set(board.viewCount, board.viewCount.add(1))
                .where(board.id.eq(boardId))
                .execute();
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        log.info("pageable: " + pageable);
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdDate":
                        OrderSpecifier<?> orderCreatedDate = QueryDslUtil.getSortedColumn(direction, board, "createdDate");
                        orders.add(orderCreatedDate);
                        break;
                    case "viewCount":
                        OrderSpecifier<?> orderViewCount = QueryDslUtil.getSortedColumn(direction, board, "viewCount");
                        orders.add(orderViewCount);
                        break;
                    case "likeCount":
                        OrderSpecifier<?> orderLikeCount = QueryDslUtil.getSortedColumn(direction, board, "likeCount");
                        orders.add(orderLikeCount);
                        break;
                    default:
                        break;
                }
            }
        }
        return orders;
    }
}
