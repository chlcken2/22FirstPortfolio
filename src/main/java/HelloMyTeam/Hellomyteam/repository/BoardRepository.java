package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board getBoardById(Long boardId);
}
