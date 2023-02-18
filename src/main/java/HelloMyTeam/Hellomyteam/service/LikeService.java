package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Like;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.repository.BoardRepository;
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
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final LikeCustomImpl likeCustomImpl;
    private final EntityManager em;

    public boolean checkLike(Long teamMemberInfoId, Long boardId) {
        TeamMemberInfo teamMemberInfo = teamMemberInfoRepository.findById(teamMemberInfoId)
                .orElseThrow(() -> new IllegalArgumentException("TeamMemberInfo Id가 없습니다"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판 Id가 없습니다"));

        if (isNotAlreadyLike(teamMemberInfo, board)) {
            likeRepository.save(new Like(teamMemberInfo, board));
            setLikeCount(boardId);
            return true;
        }
        likeRepository.deleteLikeByTeamMemberInfoAndBoard(teamMemberInfo, board);
        setLikeCount(boardId);
        return false;
    }

    private Boolean isNotAlreadyLike(TeamMemberInfo teamMemberInfo, Board board) {
         Like like = likeCustomImpl.existsLikeByIds(teamMemberInfo, board);
        if (like != null) {
            return false;
        }
        return true;
    }

    private void setLikeCount(Long boardId) {
        Integer count = likeRepository.countLikeByBoardId(boardId);
        Board findBoard = em.find(Board.class, boardId);
        findBoard.setLikeCount(count);
    }
}

