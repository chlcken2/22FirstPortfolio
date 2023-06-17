package hellomyteam.hellomyteam.entity;

import lombok.*;
import javax.persistence.*;

@Entity(name = "TeamMemberCondition")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberCondition {

    @Id
    @GeneratedValue
    @Column(name = "teammember_condition_id")
    private Long id;

    private String condition1;
    private String condition2;
    private String condition3;
    private String condition4;
    private String condition5;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMemberInfo_id")
    private TeamMemberInfo teamMemberInfo;
}
