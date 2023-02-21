package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
}
