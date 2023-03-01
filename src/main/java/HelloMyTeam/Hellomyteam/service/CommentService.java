package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.CommentReqDto;
import HelloMyTeam.Hellomyteam.dto.CommentResDto;
import HelloMyTeam.Hellomyteam.dto.CommonResponse;
import HelloMyTeam.Hellomyteam.entity.*;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import HelloMyTeam.Hellomyteam.repository.BoardRepository;
import HelloMyTeam.Hellomyteam.repository.CommentReplyRepository;
import HelloMyTeam.Hellomyteam.repository.CommentRepository;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.CommentCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentCustomImpl commentCustomImpl;
    private final EntityManager em;


    public CommonResponse<?> findCommentsByBoard(Long boardId) {
        Board findBoard = boardService.getBoardById(boardId);
        if (null == findBoard) {
            return CommonResponse.createError("존재하지 않는 게시글 id입니다.");
        }

        List<Comment> comments = commentCustomImpl.findAllByBoard(findBoard);
        List<CommentResDto> result = new ArrayList<>();
        Map<Long, CommentResDto> map = new HashMap<>();


        return CommonResponse.createSuccess(result, "조회 성공");
    }


    public CommonResponse<?> createComment(Long boardId, CommentReqDto commentReqDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("boardId가 누락되었습니다."));
        if (board == null) {
            return CommonResponse.createError("존재하지 않는 게시글입니다.");
        }

        Comment parent = null;
        //자식 댓글 존재시
        if (commentReqDto.getParentId() != null) {
            parent = commentRepository.findCommentById(commentReqDto.getParentId());

            if (null == parent) {
                return CommonResponse.createError("부모댓글과 자식 댓글이 일치하지 않습니다.");
            }
            if (parent.getBoard().getId() != boardId) {
                return CommonResponse.createError("부모댓글과 자식 댓글 게시글 번호가 일치하지 않습니다.");
            }
        }

        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findTeamMemberInfoById(commentReqDto.getTeamMemberInfoId());
        Comment comment = Comment.builder()
                .teamMemberInfo(teamMemberInfo)
                .board(board)
                .writer(teamMemberInfo.getMember().getName())
                .commentStatus(BoardAndCommentStatus.NORMAL)
                .content(commentReqDto.getContent())
                .build();

        if (null != parent) {
            comment.updateParent(parent);
        }
        commentRepository.save(comment);

        CommentResDto commentResDto = null;
        if (parent != null) {
            commentResDto = CommentResDto.builder()
                    .commentId(comment.getId())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .parentId(comment.getParent().getId())
                    .build();
        } else {
            commentResDto = CommentResDto.builder()
                    .commentId(comment.getId())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
        }
        return CommonResponse.createSuccess(commentReqDto, "댓글 작성 success");
    }

    public Comment updateComment(Long commentId, CommentReqDto commentReqDto) {
        Comment findComment = em.find(Comment.class, commentId);
        if (findComment.getTeamMemberInfo().getId() != commentReqDto.getTeamMemberInfoId()) {
            return null;
        }
        findComment.setContent(commentReqDto.getContent());
        return findComment;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
