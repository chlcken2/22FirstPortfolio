package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Boolean existsByEmail(String email);
}