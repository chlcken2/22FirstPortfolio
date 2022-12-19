package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Member")
@Getter
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String mobile;                             //핸드폰번호
    private String email;                              //XXX@gmail.com
    private String memberName;                         //닉네임 -> 이름 사용 고정
    private String birthday;
    private MemberStatus memberStatus;                 //0-정상, 1-중지, 2-탈퇴, 3-경고, 4-강퇴, 5-불법
    private int termsOfServiceYn;                      //0-미수신, 1-수신
    private int privacyYn;                             //0-미수신, 1-수신

    @ManyToOne
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
}
