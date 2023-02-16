package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
public class BoardResDto {
    private String writer;
    private String title;
    private BoardAndCommentStatus boardStatus;
    private Integer viewNo;
    private LocalDateTime createdDate;
    private Integer commentCount;

    @QueryProjection
    public BoardResDto(String writer, String title, BoardAndCommentStatus boardStatus, Integer viewNo, LocalDateTime createdDate, Integer commentCount) {
        this.writer = writer;
        this.title = title;
        this.boardStatus = boardStatus;
        this.viewNo = viewNo;
        this.createdDate = createdDate;
        this.commentCount = commentCount;
    }
}
