package hellomyteam.hellomyteam.api;

import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.dto.CommonResponse;
import hellomyteam.hellomyteam.service.BoardService;
import hellomyteam.hellomyteam.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    /**
     * 보드 crud
     * 보드 종류 공지 / 자유
     * 게시글 검색 필터 (좋아요, 최신순, 7일, 30일, 전 -> 네이버카페 참고)
     * 조회수, 좋아요
     * 페이징 카운트 쿼리
     */

    private final BoardService boardService;
    private final LikeService likeService;
    //TODO 검색 정렬 조건 추가 필
//    @ApiOperation(value = "게시판 목록 조회", notes = "teamId로 팀 별 게시판을 조회한다. baseUrl/?page={page}&size={size}&sort={sort},DESC")
//    @GetMapping("/team/{teamId}/boards")
//    public CommonResponse<?> getBoards(@PathVariable Long teamId,
//                                       @RequestParam(value = "boardCategory") BoardCategory boardCategory,
//                                       @RequestParam BoardSearchReqDto boardSearchReqDto,
//                                       @RequestParam BoardSearch2ReqDto boardSearch2ReqDto,
//                                       @PageableDefault(value = 20) Pageable pageable
//                                       ) {
//        Page<BoardListResDto> boards = boardService.getBoards(teamId, boardCategory, boardSearchReqDto, boardSearch2ReqDto, pageable);
//        return CommonResponse.createSuccess(boards, "게시판 리스트 가져오기 success");
//    }

    @ApiOperation(value = "게시판 작성", notes = "teamMemberInfo_id가 존재해야한다, 게시판을 생성한다.")
    @PostMapping("/board")
    public CommonResponse<?> writeBoard(@RequestBody BoardWriteDto boardWriteDto) {
        Board board = boardService.createBoard(boardWriteDto);
        return CommonResponse.createSuccess(board, "게시판 작성 success");
    }

    @ApiOperation(value = "게시판 상세 조회", notes = "boardId로 게시판 상세 조회, 쿠키를 통한 조회수 중복체크 처리 포함")
    @GetMapping("/board/{boardid}")
    public CommonResponse<?> detailBoard(@PathVariable(value = "boardid") Long boardId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        boardService.updateView(boardId, request, response);
        BoardDetailResDto board = boardService.getBoard(boardId);
        return CommonResponse.createSuccess(board, "게시판 상세정보 가져오기 success");
    }

    @ApiOperation(value = "게시판 상세 수정", notes = "boardId로 게시판 수정하기")
    @PutMapping("/board/{boardid}")
    public CommonResponse<?> updateBoard(@PathVariable(value = "boardid") Long boardId,
                                         @RequestBody BoardUpdateDto boardUpdateDto) {
        Board board = boardService.updateBoard(boardId, boardUpdateDto);
        return CommonResponse.createSuccess(board, "게시판 수정하기 success");
    }

    @ApiOperation(value = "게시판 삭제", notes = "boardId로 게시판 삭제하기")
    @DeleteMapping("/board/{boardid}")
    public CommonResponse<?> deleteBoard(@PathVariable(value = "boardid") Long boardId) {
        boardService.deleteBoard(boardId);
        return CommonResponse.createSuccess("보드 삭제 success");
    }

    @ApiOperation(value = "게시판 좋아요/취소", notes = "좋아요 클릭시 true 리턴, 이후 클릭시 좋아요 해제하고 false리턴")
    @PostMapping("/board/{boardid}/like")
    public CommonResponse<?> isLikeBoard(@PathVariable(value = "boardid") Long boardId,
                                          @RequestBody LikeReqDto likeReqDto) {
        Boolean bool = likeService.checkLikeBoard(likeReqDto.getTeamMemberInfoId(), boardId);
        return CommonResponse.createSuccess(bool, "좋아요 true/false");
    }
}
