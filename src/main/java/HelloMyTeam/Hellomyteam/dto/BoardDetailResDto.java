package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDetailResDto {
    private String writer;
    private String title;
    private BoardAndCommentStatus boardStatus;
    private BoardCategory boardCategory;
    private String contents;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdDate;

    @QueryProjection
    public BoardDetailResDto(String writer, String title, BoardAndCommentStatus boardStatus, BoardCategory boardCategory, String contents, Integer viewCount, Integer likeCount, LocalDateTime createdDate) {
        this.writer = writer;
        this.title = title;
        this.boardStatus = boardStatus;
        this.boardCategory = boardCategory;
        this.contents = contents;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
    }
}
