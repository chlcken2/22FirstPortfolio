package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Board;
import HelloMyTeam.Hellomyteam.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByBoard(Board board);
}
