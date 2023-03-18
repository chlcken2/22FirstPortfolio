package hellomyteam.hellomyteam.service;

import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.TermsAndCond;
import hellomyteam.hellomyteam.entity.status.JoinPurpose;
import hellomyteam.hellomyteam.entity.status.MemberStatus;
import hellomyteam.hellomyteam.entity.status.TermsAndCondStatus;
import hellomyteam.hellomyteam.exception.BadRequestException;
import hellomyteam.hellomyteam.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import javax.transaction.Transactional;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Before
    @Rollback
    public void 회원가입() throws Exception {
        //given
        String encodePassword = passwordEncoder.encode("test1234");
        Member member = Member.builder()
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

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Assertions.assertEquals(member, savedMember);
        Assertions.assertNotEquals(encodePassword, "test1234");
        org.assertj.core.api.Assertions.assertThat(member).isEqualTo(savedMember);
        System.out.println("savedMember.getId() = " + savedMember.getId());
    }

    @Test
    @Rollback
    public void 이메일_중복체크() throws Exception {
        //given
        String encodePassword = passwordEncoder.encode("test1234");
        Member member1 = Member.builder()
                .email("test@email.com")
                .password(encodePassword)
                .name("test")
                .birthday("2023-03-01")
                .memberStatus(MemberStatus.NORMAL)
                .joinPurpose(JoinPurpose.TEAM_CREATE)
                .termsAndCond(new ArrayList<>())
                .build();

        member1.addTermsAndCond(TermsAndCond.builder()
                .termsOfServiceYn(TermsAndCondStatus.YES)
                .privacyYn(TermsAndCondStatus.YES)
                .build());

        //when
        if (memberRepository.existsByEmail(member1.getEmail())) {
            new BadRequestException("Email address already in use.");
            return;
        }
        //then
        Assert.fail("중복검사에 따른 예외가 발생해야한다.: fail까지 도착하면 안됨");
    }
}