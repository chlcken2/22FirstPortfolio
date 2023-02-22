package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.BoardDetailResDto;
import HelloMyTeam.Hellomyteam.dto.CommentReqDto;
import HelloMyTeam.Hellomyteam.dto.CommentResDto;
import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.CommentReply;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final BoardService boardService;
    private final CommentCustomImpl commentCustomImpl;
    private final EntityManager em;


    public List<Comment> findCommentsByBoard(Board board) {
//        List<CommentResDto> comments = commentCustomImpl.findCommentsByBoardId(boardId);
        List<Comment> comments = commentRepository.findCommentsByBoard(board);
        return comments;
    }

    public Comment createCommentByBoard(Long boardId, CommentReqDto commentReq) {
        Board board = boardService.getBoardById(boardId);

        TeamMemberInfo findTeamMemberInfo = teamMemberInfoRepository.findById(commentReq.getTeamMemberInfoId())
                .orElseThrow(()-> new IllegalStateException("teamMemberInfo id가 누락되었습니다."));

        Comment comment = Comment.builder()
                .commentStatus(BoardAndCommentStatus.NORMAL)
                .writer(findTeamMemberInfo.getMember().getName())
                .content(commentReq.getContent())
                .teamMemberInfo(findTeamMemberInfo)
                .board(board)
                .build();

        return commentRepository.save(comment);
    }

//    public CommentReply createCommentReplyByParentId(Long parentId, CommentReqDto commentReq) {
//        Comment findComment = commentRepository.findById(parentId)
//                .orElseThrow(()-> new IllegalStateException("parent id가 누락되었습니다."));;
//
//        TeamMemberInfo findTeamMemberInfo = teamMemberInfoRepository.findById(commentReq.getTeamMemberInfoId())
//                .orElseThrow(()-> new IllegalStateException("teamMemberInfo id가 누락되었습니다."));
//
//        CommentReply commentReply = CommentReply.builder()
//                .content(commentReq.getContent())
//                .commentReplyStatus(BoardAndCommentStatus.NORMAL)
//                .teamMemberInfo(findTeamMemberInfo)
//                .comment(findComment)
//                .build();
//        return commentReplyRepository.save(commentReply);
//    }

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
