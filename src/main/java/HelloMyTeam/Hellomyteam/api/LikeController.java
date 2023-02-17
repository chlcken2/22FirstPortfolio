package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @ApiOperation(value = "게시판 좋아요/취소", notes = "좋아요 클릭시 true 리턴, 이후 클릭시 좋아요 해제하고 false리턴")
    @PostMapping("/board/like/{boardId}/")
    public CommonResponse<?> addLike(@PathVariable Long boardId,
                                     @RequestParam(required = true, value = "teamMemberInfoId") Long teamMemberInfoId) {
        Boolean bool = likeService.checkLike(teamMemberInfoId, boardId);
        return CommonResponse.createSuccess(bool, "보드 삭제 success");
    }

    //댓글 좋아요/취소
}
