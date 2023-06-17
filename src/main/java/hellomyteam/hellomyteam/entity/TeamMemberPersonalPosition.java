package hellomyteam.hellomyteam.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberPersonalPosition {

    @Id
    @GeneratedValue
    @Column(name = "teammember_personal_position_id")
    private Long id;

    private String position1;
    private String position2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;
}
