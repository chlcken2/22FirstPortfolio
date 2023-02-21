package HelloMyTeam.Hellomyteam.dto;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.CommentReply;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeEntityTypeDto {
    private Board board;
    private Comment comment;
    private CommentReply commentReply;
}
