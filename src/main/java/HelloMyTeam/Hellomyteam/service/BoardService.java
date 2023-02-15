package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.BoardUpdateDto;
import HelloMyTeam.Hellomyteam.dto.BoardWriteDto;
import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.BoardCategory;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.entity.status.BoardAndCommentStatus;
import HelloMyTeam.Hellomyteam.repository.BoardRepository;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.BoardCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final BoardCustomImpl boardCustomImpl;
    private final EntityManager em;

    public Board createBoard(BoardWriteDto boardWriteDto) {
        log.info("boardWriteDto" + boardWriteDto);
        TeamMemberInfo findTeamMemberInfo = teamMemberInfoRepository.findById(boardWriteDto.getTeamMemberInfoId())
                .orElseThrow(()-> new IllegalStateException("teamMemberInfo id가 누락되었습니다."));

        Board board = Board.builder()
                .boardCategory(boardWriteDto.getBoardCategory())
                .writer(findTeamMemberInfo.getMember().getName())
                .title(boardWriteDto.getTitle())
                .contents(boardWriteDto.getContents())
                .boardStatus(BoardAndCommentStatus.NORMAL)
                .teamMemberInfo(findTeamMemberInfo)
//                .likeNo(0)
                .build();
        return boardRepository.save(board);
    }

    public List<Board> getBoards(Long teamId, BoardCategory boardCategory) {
        List<Board> boards = boardCustomImpl.findBoardsByTeamId(teamId, boardCategory);
        return boards;
    }

    public Board getBoard(Long id) {
        Board findBoard = boardRepository.findBoardById(id);
        return findBoard;
    }


    public Board updateBoard(Long boardId, BoardUpdateDto boardUpdateDto) {
        Board findBoard = em.find(Board.class, boardId);
        findBoard.setBoardCategory(boardUpdateDto.getChangeBoardCategory());
        findBoard.setContents(boardUpdateDto.getChangeContents());
        findBoard.setTitle(boardUpdateDto.getChangeTitle());
        return findBoard;
    }
}
