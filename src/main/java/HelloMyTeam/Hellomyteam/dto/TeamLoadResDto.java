package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Builder
@Getter
public class TeamLoadResDto {
    private String teamName;
    private Long teamId;
    private List<Board> noticeBoard = new ArrayList<>();
    private List<Board> freeBoard = new ArrayList<>();
    private List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();

    @QueryProjection
    public TeamLoadResDto(String teamName, Long teamId, List<Board> noticeBoard, List<Board> freeBoard, List<TeamMemberInfo> teamMemberInfos) {
        this.teamName = teamName;
        this.teamId = teamId;
        this.noticeBoard = noticeBoard;
        this.freeBoard = freeBoard;
        this.teamMemberInfos = teamMemberInfos;
    }
}
