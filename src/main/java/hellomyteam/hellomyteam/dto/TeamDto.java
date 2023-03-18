package hellomyteam.hellomyteam.dto;

import hellomyteam.hellomyteam.entity.status.team.TacticalStyleStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private Long memberId;
    private String teamName;
    private String oneIntro;
    private String detailIntro;
    private TacticalStyleStatus tacticalStyleStatus;
    private MultipartFile imgFile;
}
