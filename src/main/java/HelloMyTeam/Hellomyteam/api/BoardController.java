package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.BoardDetailResDto;
import HelloMyTeam.Hellomyteam.dto.BoardListResDto;
import HelloMyTeam.Hellomyteam.dto.BoardUpdateDto;
import HelloMyTeam.Hellomyteam.dto.BoardWriteDto;
import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @ApiOperation(value = "게시판 목록 조회", notes = "teamId로 팀 별 게시판을 조회한다. 페이징 처리 추가")
    @GetMapping("/team/{teamId}/boards")
    public CommonResponse<?> getBoards(@PathVariable Long teamId,
                                       @RequestParam(value = "boardCategory") BoardCategory boardCategory,
                                       @PageableDefault(20) Pageable pageable) {
        Page<BoardListResDto> boards = boardService.getBoards(teamId, boardCategory, pageable);
        return CommonResponse.createSuccess(boards, "게시판 리스트 가져오기 success");
    }

    @ApiOperation(value = "게시판 작성", notes = "teamMemberInfo_id가 존재해야한다, 게시판을 생성한다.")
    @PostMapping("/board/write")
    public CommonResponse<?> writeBoard(@RequestBody BoardWriteDto boardWriteDto) {
        Board board = boardService.createBoard(boardWriteDto);
        return CommonResponse.createSuccess(board, "게시판 작성 success");
    }

    //TODO 게시판 댓글 가져오기
    @ApiOperation(value = "게시판 상세 조회", notes = "boardId로 게시판 상세 조회, 쿠키를 통한 조회수 중복체크 처리 포함")
    @GetMapping("/board/{boardId}")
    public CommonResponse<?> detailBoard(@PathVariable Long boardId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        boardService.updateView(boardId, request, response);
        BoardDetailResDto board = boardService.getBoard(boardId);
        return CommonResponse.createSuccess(board, "게시판 상세정보 가져오기 success");
    }

    @ApiOperation(value = "게시판 상세 수정", notes = "boardId로 게시판 수정하기")
    @PutMapping("/board/{boardId}")
    public CommonResponse<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateDto boardUpdateDto) {
        Board board = boardService.updateBoard(boardId, boardUpdateDto);
        return CommonResponse.createSuccess(board, "게시판 수정하기 success");
    }

    //TODO 관련된 댓글, 대댓글 삭제 "연관관계 매핑" 확인할 것
    @ApiOperation(value = "게시판 삭제", notes = "boardId로 게시판 삭제하기")
    @DeleteMapping("/board/{boardId}")
    public CommonResponse<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return CommonResponse.createSuccess("보드 삭제 success");
    }

    @ApiOperation(value = "게시판 좋아요 수 조회", notes = "좋아요 숫자 노출될 곳에 해당 API 이용(게시판 리스트, 게시판 상세)")
    @GetMapping("/board/{boardId}/like")
    public CommonResponse<?> getBoardLikeCount(@PathVariable Long boardId) {
        Integer count = boardService.findBoardLikeCount(boardId);
        return CommonResponse.createSuccess(count, "좋아요 수");
    }

//    @ApiOperation(value = "게시판 조회수 조회", notes = "좋아요 숫자 노출될 곳에 해당 API 이용(게시판 리스트, 게시판 상세)")
//    @GetMapping("/board/{boardId}/hit")
//    public CommonResponse<?> getBoardHitsCount(@PathVariable Long boardId) {
//        Integer count = boardService.findBoardLikeCount(boardId);
//        return CommonResponse.createSuccess(count, "좋아요 수");
//    }

    // 게시판 검색 조건
    // 조회수, 좋아요, 기간(1주, 1달, 1년, 전체)
//    @GetMapping("/board/search")
//    public CommonResponse<?> searchcriteria(){
//
//        return CommonResponse.createSuccess("보드 삭제 success");
//    }





}
