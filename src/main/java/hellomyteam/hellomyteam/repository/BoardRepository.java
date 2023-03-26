package hellomyteam.hellomyteam.repository;

import hellomyteam.hellomyteam.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board getBoardById(Long boardId);

    @Query(value=
                    "select * " +
                    "from board " +
                    "where team_id = :teamId " +
                    "and board_category = :category " +
                    "order by created_date desc limit :pageNum, 10"
                    ,
            nativeQuery = true)
    List<Board> getBoards(Long teamId, int pageNum, String category);

    @Query(value=
            "select count(*) " +
                    "from board " +
                    "where team_id = :teamId " +
                    "and board_category = :category "
            ,
            nativeQuery = true)
    int getBoardTotalCount(Long teamId, String category);
}
