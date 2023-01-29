package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.TeamParam;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.entity.TermsAndCond;
import HelloMyTeam.Hellomyteam.entity.status.JoinPurpose;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import HelloMyTeam.Hellomyteam.entity.status.TermsAndCondStatus;
import HelloMyTeam.Hellomyteam.entity.status.team.TacticalStyleStatus;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;

import static HelloMyTeam.Hellomyteam.entity.status.team.AuthorityStatus.LEADER;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TeamServiceTest {

    //db 반영시 flush 선언
    @Autowired
    EntityManager em;
    @Autowired
    TeamService teamService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;



    @Test
    public void 팀생성_with_고유번호() throws Exception{
        //given
        //회원가입 정보 입력
        String encodePassword = passwordEncoder.encode("test1234");
        Member member = Member.builder()
                .mobile("010-0000-0000")
                .email("test@email.com")
                .password(encodePassword)
                .name("test")
                .birthday("2023-03-01")
                .memberStatus(MemberStatus.NORMAL)
                .joinPurpose(JoinPurpose.TEAM_CREATE)
                .termsAndCond(new ArrayList<>())
                .build();

        member.addTermsAndCond(TermsAndCond.builder()
                .termsOfServiceYn(TermsAndCondStatus.YES)
                .privacyYn(TermsAndCondStatus.YES)
                .build());

        //회원가입 정보 저장
        Member savedMember = memberRepository.save(member);

        //팀 가입 정보 입력
        TeamParam team = TeamParam.builder()
                .memberId(savedMember.getId())
                .teamName("테스트용 팀 이름")
                .oneIntro("팀에 대한 한 줄 소개입니다.")
                .detailIntro("상세소개!!~_대환영!!!입니다...ㅇ.ㅁㄴㅇㅁㄴㅇasd123-=`-'/.,")
                .tacticalStyleStatus(TacticalStyleStatus.POSSESSION)
                .build();

        //팀 가입 정보 저장 - 팀 생성
        Team savedTeam = teamService.createTeamWithAuthNo(team);

        //when
        //team_member_info 등록 - teamMemberInfo 생성 (연관관계 매핑)
        TeamMemberInfo teamMemberInfo  = teamService.teamMemberInfoSaveAuthLeader(savedTeam, savedMember);

        //then
        //첫 팀 생성시 "LEADER", 팀원 수 = 1
        Assertions.assertThat(teamMemberInfo.getTeam().getMemberCount()).isEqualTo(1);
        Assertions.assertThat(teamMemberInfo.getAuthority()).isEqualTo(LEADER);
        Assertions.assertThat(teamMemberInfo.getMember()).isEqualTo(savedMember);
        Assertions.assertThat(teamMemberInfo.getTeam()).isEqualTo(savedTeam);
        Assertions.assertThat(teamMemberInfo.getTeam().getTeamName()).isEqualTo("테스트용 팀 이름");
        Assertions.assertThat(teamMemberInfo.getTeam().getTeamSerialNo()).isEqualTo(savedTeam.getTeamSerialNo());
    }

    @Test
    public void 팀생성시_팀리더_권한으로_teamMemberInfo저장() throws Exception {

    }

    @Test
    public void 고유번호_팀이름을_통한_검색() {
    }

    @Test
    public void findTeamById() {
    }

    @Test
    public void joinTeamAuthWait() {
    }

    @Test
    public void acceptTeamMemberById() {
    }

    @Test
    public void findTeamByTeamMemberId() {
    }
}