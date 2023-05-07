package hellomyteam.hellomyteam.repository;

import hellomyteam.hellomyteam.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FileUploadRepository extends JpaRepository<Image, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value =
            "update image i " +
                    "set i.store_filename = :storeFilename, " +
                    "i.image_url = :imageUrl " +
                    "where i.team_id = :teamId "
            ,
            nativeQuery = true)
    Image updateLogoByTeam(@Param("teamId") Long teamId,
                           @Param("imageUrl") String imageUrl,
                           @Param("storeFilename") String storeFilename);

    Boolean existsImageByTeamId(Long teamId);

    Boolean existsImageByTeamMemberInfoId(Long teamMemberInfoId);

    List<Image> findImageByTeamId(Long teamId);
}
