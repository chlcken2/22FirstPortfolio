package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.CommentReply;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.service.BoardService;
import HelloMyTeam.Hellomyteam.service.CommentService;
import HelloMyTeam.Hellomyteam.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final LikeService likeService;

    //TODO 계층형 댓글 or 일반 댓글 형식 정해야 함
    @ApiOperation(value = "댓글 가져오기", notes = "게시글에 대한 댓글 가져오기")
    @GetMapping("/board/{boardId}/comment")
    public CommonResponse<?> getBoardComment(@PathVariable Long boardId) {
        Board board = boardService.getBoardById(boardId);
        List<Comment> comments = commentService.findCommentsByBoard(board);
        return CommonResponse.createSuccess(comments, "게시판 작성 success");
    }

    @ApiOperation(value = "댓글 작성", notes = "teamMemberInfo Id 존재해야한다. *Id로 이름 매핑")
    @PostMapping("/board/{boardId}/comment")
    public CommonResponse<?> writeBoardComment(@PathVariable Long boardId, @RequestBody CommentReqDto commentReq) {
        Comment comment = commentService.createCommentByBoard(boardId, commentReq);
        return CommonResponse.createSuccess(comment, "게시판 작성 success");
    }

//    @ApiOperation(value = "대댓글 작성", notes = "teamMemberInfo Id 존재해야한다. *Id로 이름 매핑")
//    @PostMapping("/comment/{parentId}")
//    public CommonResponse<?> writeBoardCommentReply(@PathVariable Long parentId, @RequestBody CommentReqDto commentReq) {
//        CommentReply commentReply = commentService.createCommentReplyByParentId(parentId, commentReq);
//        return CommonResponse.createSuccess(commentReply, "게시판 작성 success");
//    }

    @ApiOperation(value = "댓글 수정", notes = "teamMemberInfo Id 존재해야한다. *Id로 이름 매핑")
    @PutMapping("/comment/{commentId}")
    public CommonResponse<?> updateBoardComment(@PathVariable Long commentId,
                                                @RequestBody CommentReqDto commentReqDto) {
        Comment comment = commentService.updateComment(commentId, commentReqDto);
        return CommonResponse.createSuccess(comment, "게시판 작성 success");
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 후 새로고침 적용")
    @DeleteMapping("/comment/{commentId}")
    public CommonResponse<?> deleteBoardComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return CommonResponse.createSuccess("댓글 삭제 success");
    }

    @ApiOperation(value = "댓글 좋아요/취소", notes = "좋아요 클릭시 true 리턴, 이후 클릭시 좋아요 해제하고 false리턴")
    @PostMapping("/comment/like/{commentId}/")
    public CommonResponse<?> isLikeComment(@PathVariable Long commentId,
                                          @RequestBody LikeReqDto likeReqDto) {
        Boolean bool = likeService.checkLikeComment(likeReqDto.getTeamMemberInfoId(), commentId);
        return CommonResponse.createSuccess(bool, "좋아요 true/false");
    }
}
