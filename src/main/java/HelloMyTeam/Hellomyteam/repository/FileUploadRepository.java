package HelloMyTeam.Hellomyteam.repository;

import HelloMyTeam.Hellomyteam.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<Image, Long> {
}
