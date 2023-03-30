package hellomyteam.hellomyteam.api;

import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.service.CommentService;
import hellomyteam.hellomyteam.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;

    @ApiOperation(value = "댓글 가져오기", notes = "게시글에 대한 계층형 댓글 가져오기, 삭제된 게시글 상태변경, 값 변경 처리 적용")
    @GetMapping("/boards/{boardid}/comment")
    public CommonResponse<?> getBoardComments(@PathVariable(value = "boardid") Long boardId) {
        return commentService.findCommentsByBoard(boardId);
    }

    @ApiOperation(value = "댓글 작성", notes = "parentId가 null일 경우: 부모, parentId존재: 자식 댓글")
    @PostMapping("/boards/{boardid}/comment")
    public CommonResponse<?> writeBoardComment(@PathVariable(value = "boardid") Long boardId, @RequestBody CommentCreateReqDto commentCreateReqDto) {
        return commentService.createComment(boardId, commentCreateReqDto);
    }

    @ApiOperation(value = "댓글 수정", notes = "teamMemberInfo Id 존재해야한다. *Id로 이름 매핑")
    @PutMapping("/comments/{commentid}")
    public CommonResponse<?> updateBoardComment(@PathVariable(value = "commentid") Long commentId,
                                                @RequestBody CommentUpdateReqDto commentUpdateReqDto) {
        return commentService.updateComment(commentId, commentUpdateReqDto);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 '관리자 삭제'로 상태 변경")
    @DeleteMapping("/comments/{commentid}")
    public CommonResponse<?> deleteBoardComment(@PathVariable(value = "commentid") Long commentId) {
        commentService.deleteComment(commentId);
        return CommonResponse.createSuccess("댓글 삭제 success");
    }

    @ApiOperation(value = "댓글 좋아요/취소", notes = "좋아요 클릭시 true 리턴, 이후 클릭시 좋아요 해제하고 false리턴")
    @PostMapping("/comments/{commentid}/like")
    public CommonResponse<?> isLikeComment(@PathVariable(value = "commentid") Long commentId,
                                          @RequestBody LikeReqDto likeReqDto) {
        Boolean bool = likeService.checkLikeComment(likeReqDto.getTeamMemberInfoId(), commentId);
        return CommonResponse.createSuccess(bool, "좋아요 true/false");
    }
}
