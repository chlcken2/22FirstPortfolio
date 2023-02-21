package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Comment;
import HelloMyTeam.Hellomyteam.entity.Like;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.repository.BoardRepository;
import HelloMyTeam.Hellomyteam.repository.CommentRepository;
import HelloMyTeam.Hellomyteam.repository.LikeRepository;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.LikeCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final LikeCustomImpl likeCustomImpl;
    private final EntityManager em;

    public boolean checkLikeBoard(Long teamMemberInfoId, Long boardId) {
        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findById(teamMemberInfoId)
                .orElseThrow(() -> new IllegalArgumentException("TeamMemberInfo Id가 없습니다"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판 Id가 없습니다"));

        if (isNotAlreadyBoardLike(teamMemberInfo, board)) {
            likeRepository.save(new Like(teamMemberInfo, board));
            setBoardLikeCount(boardId);
            return true;
        }
        likeRepository.deleteLikeByTeamMemberInfoAndBoard(teamMemberInfo, board);
        setBoardLikeCount(boardId);
        return false;
    }


    public Boolean checkLikeComment(Long teamMemberInfoId, Long commentId) {
        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findById(teamMemberInfoId)
                .orElseThrow(() -> new IllegalArgumentException("TeamMemberInfo Id가 없습니다"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 Id가 없습니다"));

        if (isNotAlreadyCommentLike(teamMemberInfo, comment)) {
            likeRepository.save(new Like(teamMemberInfo, comment));
            setCommentLikeCount(commentId);
            return true;
        }
        likeRepository.deleteLikeByTeamMemberInfoAndComment(teamMemberInfo, comment);
        setCommentLikeCount(commentId);
        return false;
    }

    private Boolean isNotAlreadyBoardLike(TeamMemberInfo teamMemberInfo, Board board) {
         Like like = likeCustomImpl.existsBoardLikeByIds(teamMemberInfo, board);
        if (like != null) {
            return false;
        }
        return true;
    }

    private Boolean isNotAlreadyCommentLike(TeamMemberInfo teamMemberInfo, Comment comment) {
        Like like = likeCustomImpl.existsCommentLikeByIds(teamMemberInfo, comment);
        if (like != null) {
            return false;
        }
        return true;
    }

    private void setBoardLikeCount(Long boardId) {
        Integer count = likeRepository.countLikeByBoardId(boardId);
        Board findBoard = em.find(Board.class, boardId);
        findBoard.setLikeCount(count);
    }

    private void setCommentLikeCount(Long commentId) {
        Integer count = likeRepository.countLikeByCommentId(commentId);
        Comment findComment = em.find(Comment.class, commentId);
        findComment.setLikeCount(count);
    }
}

