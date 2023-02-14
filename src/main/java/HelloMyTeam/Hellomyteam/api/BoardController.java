package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    /**
     * 보드 crud
     * 보드 종류 공지 / 자유
     * 게시글 검색 필터 (좋아요, 최신순, 7일, 30일, 전 -> 네이버카페 참고)
     * 조회수, 좋아요
     * 페이징 카운트 쿼리
     */

    //게시판 목록
    @GetMapping("/")
    public CommonResponse<?> getBoards(){

        return CommonResponse.createSuccess("게시판 정보 가져오기 success");
    }

    //게시판 작성
    @PostMapping("/write")
    public CommonResponse<?> writeBoard(){

        return CommonResponse.createSuccess("게시판 작성 success");
    }

    //게시판 상세 보기
    @PostMapping("/write/{boardId}")
    public CommonResponse<?> detailBoard(){

        return CommonResponse.createSuccess("게시판 상세정보 가져오기 success");
    }

    //게시판 수정
    @PutMapping("/write/{boardId}")
    public CommonResponse<?> updateBoard(){

        return CommonResponse.createSuccess("게시판 수정하기 success");
    }

    //게시판 삭제
    @DeleteMapping("/write/{boardId}")
    public CommonResponse<?> deleteBoard(){

        return CommonResponse.createSuccess("보드 상세정보 가져오기 success");
    }

}
