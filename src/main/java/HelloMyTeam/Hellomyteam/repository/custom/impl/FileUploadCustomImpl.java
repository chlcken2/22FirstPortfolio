package HelloMyTeam.Hellomyteam.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static HelloMyTeam.Hellomyteam.entity.QImage.image;

@Repository
@RequiredArgsConstructor
public class FileUploadCustomImpl {
    private final JPAQueryFactory queryFactory;


    public void updateLogoByTeam(Long teamId, String imageUrl, String storeFilename) {
        queryFactory
                .update(image)
                .set(image.imageUrl, imageUrl)
                .set(image.storeFilename, storeFilename)
                .where(image.team.id.eq(teamId))
                .execute();
    }
}
