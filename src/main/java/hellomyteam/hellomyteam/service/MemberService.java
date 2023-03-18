package hellomyteam.hellomyteam.service;


import hellomyteam.hellomyteam.dto.*;
import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.status.MemberStatus;
import hellomyteam.hellomyteam.repository.MemberRepository;
import hellomyteam.hellomyteam.repository.custom.impl.TeamCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamCustomImpl teamCustomImpl;

    public Member findMemberByTeamInfo(TeamDto teamInfo) {
        Member member = memberRepository.findById(teamInfo.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("memberId가 누락되었습니다."));
        return member;
    }

    public Member findMemberByTeamMemberId(TeamMemberIdDto memberIdParam) {
        Member member = memberRepository.findById(memberIdParam.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("memberId가 누락되었습니다."));
        return member;
    }

    public CommonResponse<?> findJoinTeam(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (!member.isPresent()) {
            return CommonResponse.createError("가입한 회원이 아닙니다. memberId를 확인해주세요.");
        }
        if (member.get().getMemberStatus().equals(MemberStatus.WITHDRAW)) {
            return CommonResponse.createError("탈퇴한 회원입니다.");
        }
        if (member.get().getMemberStatus().equals(MemberStatus.EXIT)) {
            return CommonResponse.createError("관리자에 의해 강제 탈퇴된 회원입니다.");
        }

        List<TeamNameIdDto> findTeamMemberIds = teamCustomImpl.findTeamMemberInfoIdsByMemberId(memberId);
        if (findTeamMemberIds.isEmpty()) {
            return CommonResponse.createError("가입된 팀이 없습니다. 기본페이지로 리턴해주세요.");
        }
        return CommonResponse.createSuccess(findTeamMemberIds, "팀ID, 팀 이름 success");
    }
}
