package hellomyteam.hellomyteam.repository.custom.impl;

import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import static hellomyteam.hellomyteam.entity.QComment.comment;
import static hellomyteam.hellomyteam.entity.QImage.image;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentCustomImpl {
    private final JPAQueryFactory queryFactory;

    public List<Comment> findCommentByBoard(Board findBoard, Pageable pageable) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.board.id.eq(findBoard.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdDate.asc())
                .fetch();
    }

    public String getWriterImgUrl(Long id) {
        return queryFactory.select(image.imageUrl)
                .from(image)
                .where(image.teamMemberInfo.id.eq(id))
                .fetchOne();
    }
}
