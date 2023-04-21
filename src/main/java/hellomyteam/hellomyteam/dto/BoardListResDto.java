package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
public class BoardListResDto {
    //b.contents, b.writer, b.title, b.commentCount, b.likeCount, b.createdDate
    //b.writer, b.title, b.createdDate, b.commentCount, b.likeCount, b. contents
    private Long id;
    private String writer;
    private String title;
    private LocalDateTime createdDate;
    private Integer commentCount;
    private Integer likeCount;

    private String contents;

    @QueryProjection
    public BoardListResDto(Long id, String writer, String title, LocalDateTime createdDate, Integer commentCount, Integer likeCount, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.createdDate = createdDate;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.contents = contents;
    }
}
