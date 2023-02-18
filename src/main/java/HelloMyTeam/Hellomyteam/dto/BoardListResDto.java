package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
public class BoardListResDto {
    private String writer;
    private String title;
    private BoardAndCommentStatus boardStatus;
    private Integer viewNo;
    private LocalDateTime createdDate;
    private Integer commentCount;
    private Integer likeCount;

    @QueryProjection
    public BoardListResDto(String writer, String title, BoardAndCommentStatus boardStatus, Integer viewNo, LocalDateTime createdDate, Integer commentCount, Integer likeCount) {
        this.writer = writer;
        this.title = title;
        this.boardStatus = boardStatus;
        this.viewNo = viewNo;
        this.createdDate = createdDate;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }
}
