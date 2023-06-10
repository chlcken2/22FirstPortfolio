package hellomyteam.hellomyteam.api;

import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.dto.CommonResponse;
import hellomyteam.hellomyteam.entity.BoardCategory;
import hellomyteam.hellomyteam.entity.Image;
import hellomyteam.hellomyteam.service.BoardService;
import hellomyteam.hellomyteam.service.ImageService;
import hellomyteam.hellomyteam.service.LikeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {

    private final BoardService boardService;
    private final LikeService likeService;
    private final ImageService imageService;

    /**
     * 페이징 처리
     *  Parameter = teamId, pageNum, pageSize, category, srchType, srchKwd, sortType
     *  return = board, pageable(offset, pageSize, pageNumber, totalElements, totalPages)
     */
    @ApiOperation(value = "게시판 목록 조회 / 페이징 처리", notes = "teamId, Category, pageNum, pageSize로 게시판 조회, offset, pageSize, pageNumber, totalElements, totalPages 리턴")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teamid", value = "가져올 team Id"),
            @ApiImplicitParam(name = "pageNum", value = "페이지 번호"),
            @ApiImplicitParam(name = "pageSize", value = "보여줄 게시물 수"),
            @ApiImplicitParam(name = "category", value = "카테고리 ex)FREE_BOARD, NOTICE_BOARD"),
            @ApiImplicitParam(name = "srchType", value = "검색 타입 ex)title, contents, writer"),
            @ApiImplicitParam(name = "srchKwd", value = "검색 키워드"),
            @ApiImplicitParam(name = "sortType", value = "정렬 타입 ex)최신순, 좋아요순", defaultValue = "created_date")
    })
    @GetMapping("/teams/{teamid}/boards")
    public CommonResponse<?> getBoards(@PathVariable(value = "teamid") Long teamId,
                                       @RequestParam int pageNum,
                                       @RequestParam int pageSize,
                                       @RequestParam BoardCategory category,
                                       @RequestParam(required = false) String srchType,
                                       @RequestParam(required = false) String srchKwd,
                                       @RequestParam String sortType){

        return boardService.getBoards(teamId, pageNum, pageSize, category, srchType, srchKwd, sortType);
    }

    @ApiOperation(value = "게시판 작성", notes = "teamMemberInfo_id가 존재해야한다, 게시판을 생성한다.")
    @PostMapping("/teams/{teamid}/board")
    public CommonResponse<?> writeBoard(@PathVariable(value = "teamid") Long teamId,
                                        @RequestBody BoardWriteDto boardWriteDto) {
        Board board = boardService.createBoard(boardWriteDto);
        return CommonResponse.createSuccess(board, "게시판 작성 success");
    }

    @ApiOperation(value = "게시판 상세 조회", notes = "boardId로 게시판 상세 조회, 쿠키를 통한 조회수 중복체크 처리 포함")
    @GetMapping("/teams/{teamid}/boards/{boardid}")
    public CommonResponse<?> detailBoard(@PathVariable(value = "teamid") Long teamId,
                                         @PathVariable(value = "boardid") Long boardId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        boardService.updateView(boardId, request, response);
        BoardDetailResDto board = boardService.getBoard(boardId);
        return CommonResponse.createSuccess(board, "게시판 상세정보 가져오기 success");
    }

    @ApiOperation(value = "게시판 상세 수정", notes = "boardId로 게시판 수정하기")
    @PutMapping("/teams/{teamid}/boards/{boardid}")
    public CommonResponse<?> updateBoard(@PathVariable(value = "teamid") Long teamId,
                                         @PathVariable(value = "boardid") Long boardId,
                                         @RequestBody BoardUpdateDto boardUpdateDto) {
        Board board = boardService.updateBoard(boardId, boardUpdateDto);
        return CommonResponse.createSuccess(board, "게시판 수정하기 success");
    }

    @ApiOperation(value = "게시판 삭제", notes = "boardId로 게시판 삭제하기")
    @DeleteMapping("/teams/{teamid}/boards/{boardid}")
    public CommonResponse<?> deleteBoard(@PathVariable(value = "teamid") Long teamId,
                                         @PathVariable(value = "boardid") Long boardId) {
        boardService.deleteBoard(boardId);
        return CommonResponse.createSuccess("보드 삭제 success");
    }

    @ApiOperation(value = "게시판 좋아요/취소", notes = "좋아요 클릭시 true 리턴, 이후 클릭시 좋아요 해제하고 false리턴")
    @PostMapping("/teams/{teamid}/boards/{boardid}/like")
    public CommonResponse<?> isLikeBoard(@PathVariable(value = "teamid") Long teamId,
                                         @PathVariable(value = "boardid") Long boardId,
                                         @RequestBody LikeReqDto likeReqDto) {
        Boolean bool = likeService.checkLikeBoard(likeReqDto.getTeamMemberInfoId(), boardId);
        return CommonResponse.createSuccess(bool, "좋아요 true/false");
    }

    @ApiOperation(value = "게시판 이미지", notes = "게시판 이미지 저장 후 경로 반환")
    @PostMapping("/teams/{teamid}/board/{boardid}/image")
    public CommonResponse<?> addBoardImage(@PathVariable(value = "teamid") Long teamid,
                                           @PathVariable(value = "boardid") Long boardid,
                                           @RequestPart MultipartFile multipartFile
                                           ) throws IOException {

        return CommonResponse.createSuccess(imageService.saveBoardImage(teamid, boardid, multipartFile), "생성완료");
    }
}
