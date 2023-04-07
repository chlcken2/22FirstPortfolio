package hellomyteam.hellomyteam.repository;

import hellomyteam.hellomyteam.dto.BoardListResDto;
import hellomyteam.hellomyteam.entity.Board;
import hellomyteam.hellomyteam.entity.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board getBoardById(Long boardId);

    @Query(value = "select new hellomyteam.hellomyteam.dto.BoardListResDto(b.writer, b.title, b.createdDate, b.commentCount, b.likeCount, b. contents) " +
            "from Board b " +
            "where b.team.id = :teamId " +
            "and b.boardCategory = :category ")
    Page<BoardListResDto> getBoards(Long teamId, BoardCategory category, Pageable pageable);

    @Query(value = "select new hellomyteam.hellomyteam.dto.BoardListResDto(b.writer, b.title, b.createdDate, b.commentCount, b.likeCount, b. contents) " +
            "from Board b " +
            "where b.team.id = :teamId " +
            "and b.boardCategory = :category " +
            "and (:srchKwd is null or :srchKwd = '' or (CASE WHEN :srchType = 'writer' THEN b.writer " +
            "          WHEN :srchType = 'title' THEN b.title " +
            "          WHEN :srchType = 'contents' THEN b.contents " +
            "          ELSE '' END) " +
            "like CONCAT('%', :srchKwd, '%')) ")
    Page<BoardListResDto> srchResult(Long teamId, BoardCategory category, Pageable pageable, String srchType, String srchKwd);
}
