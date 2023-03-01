package HelloMyTeam.Hellomyteam.repository.custom.impl;

import HelloMyTeam.Hellomyteam.dto.CommentResDto;
import HelloMyTeam.Hellomyteam.dto.QBoardDetailResDto;
import HelloMyTeam.Hellomyteam.entity.Board;
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

    public List<Comment> findAllByBoard(Board findBoard) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.board.id.eq(findBoard.getId()))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdDate.asc())
                .fetch();
    }
}
