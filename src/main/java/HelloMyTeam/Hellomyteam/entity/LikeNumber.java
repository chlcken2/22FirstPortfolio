package HelloMyTeam.Hellomyteam.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikeNumber {
    @Id
    @GeneratedValue
    @Column(name = "like_number_id")
    private Long id;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "teamMemberInfo_id")
//    private TeamMemberInfo teamMemberInfo;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "board_id")
//    private Board board;

}
