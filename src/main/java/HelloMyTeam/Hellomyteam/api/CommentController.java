package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.service.BoardService;
import HelloMyTeam.Hellomyteam.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    //댓글 작성

    //댓글 수정

    //댓글 삭제

    //댓글 좋아요

    //댓글 좋아요 취소
}
