package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String mobile;

    private String email;

//    @NotNull
    private String name;

//    @NotNull
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "member")
    private List<TeamMemberInfo> teamMemberInfos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommentReply> commentReplies = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<TermsAndCond> termsAndConds = new ArrayList<>();

    @Setter
    private String refreshToken;
}
