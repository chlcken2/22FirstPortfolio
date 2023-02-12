package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.JoinPurpose;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

//    @Nullable
//    private String mobile;

    @NotNull
    private String email;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String birthday;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private JoinPurpose joinPurpose;

    @JsonIgnore
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TermsAndCond> termsAndCond = new ArrayList<>();

    @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String refreshToken;

    public void addTermsAndCond(TermsAndCond terms) {
        this.termsAndCond.add(terms);
        terms.updateMember(this);
    }
}
