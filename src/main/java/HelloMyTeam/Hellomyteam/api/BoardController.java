package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.BoardWriteDto;
import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드 crud
     * 보드 종류 공지 / 자유
     * 게시글 검색 필터 (좋아요, 최신순, 7일, 30일, 전 -> 네이버카페 참고)
     * 조회수, 좋아요
     * 페이징 카운트 쿼리
     */

    @ApiOperation(value = "게시판 목록 가져오기", notes = "teamId로 팀 별 게시판을 조회한다.")
    @GetMapping("/{teamId}")
    public CommonResponse<?> getBoards(@PathVariable Long teamId,
                                       @RequestParam(value = "boardCategory") BoardCategory boardCategory) {
        List<Board> boards = boardService.getBoards(teamId, boardCategory);
        return CommonResponse.createSuccess(boards, "게시판 리스트 가져오기 success");
    }

    @ApiOperation(value = "게시판 작성", notes = "teamMemberInfo_id가 존재해야한다, 게시판을 생성한다.")
    @PostMapping("/write")
    public CommonResponse<?> writeBoard(@RequestBody BoardWriteDto boardWriteDto){
        Board board = boardService.createBoard(boardWriteDto);
        return CommonResponse.createSuccess(board, "게시판 작성 success");
    }

//    //게시판 상세 보기
//    @GetMapping("/{boardId}")
//    public CommonResponse<?> detailBoard(){
//
//        return CommonResponse.createSuccess("게시판 상세정보 가져오기 success");
//    }
//
//    //게시판 수정
//    @PutMapping("/write/{boardId}")
//    public CommonResponse<?> updateBoard(){
//
//        return CommonResponse.createSuccess("게시판 수정하기 success");
//    }
//
//    //게시판 삭제
//    @DeleteMapping("/write/{boardId}")
//    public CommonResponse<?> deleteBoard(){
//
//        return CommonResponse.createSuccess("보드 삭제 success");
//    }
//
//    //게시판 좋아요
//    @PostMapping("/{boardId}/")
//    public CommonResponse<?> successlikeBoard(){
//
//        return CommonResponse.createSuccess("보드 삭제 success");
//    }
//
//    //게시판 좋아요취소
//    @PostMapping("/{boardId}/")
//    public CommonResponse<?> canclelikeBoard(){
//
//        return CommonResponse.createSuccess("보드 삭제 success");
//    }

}
