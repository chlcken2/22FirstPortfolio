package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.dto.CommentResDto;
import HelloMyTeam.Hellomyteam.dto.QBoardDetailResDto;
import HelloMyTeam.Hellomyteam.dto.QCommentResDto;
import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.CommentReply;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static HelloMyTeam.Hellomyteam.entity.QBoard.board;
import static HelloMyTeam.Hellomyteam.entity.QComment.comment;
import static HelloMyTeam.Hellomyteam.entity.QCommentReply.commentReply;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentCustomImpl {
    private final JPAQueryFactory queryFactory;

//    public List<Comment> findCommentByBoardId(Long boardId) {
//        return queryFactory.selectFrom(comment)
//                .leftJoin(comment.parent)
//                .fetchJoin()
//                .where(comment.board.id.eq(boardId))
//                .orderBy(
//                        comment.parent.id.asc().nullsFirst(),
//                        comment.createdDate.asc()
//                ).fetch();
//    }

//    public List<CommentResDto> findCommentsByBoardId(Long boardId) {
//        return queryFactory.select(new QCommentResDto(
//                        comment.id
//                        , comment.commentReply
//                        , comment.content
//                        , comment.writer
//                        , comment.createdDate
//                ))
//                .from(comment)
//                .where(comment.board.id.eq(boardId))
//                .where(comment.commentStatus.eq(BoardAndCommentStatus.NORMAL))
//                .orderBy(comment.createdDate.asc())
//                .fetch();
//    }
}
