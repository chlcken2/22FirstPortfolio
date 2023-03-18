package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeEntityTypeDto {
    private Board board;
    private Comment comment;
}
