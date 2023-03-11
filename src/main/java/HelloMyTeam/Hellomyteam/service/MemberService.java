package HelloMyTeam.Hellomyteam.service;


import HelloMyTeam.Hellomyteam.dto.*;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.Team;
import HelloMyTeam.Hellomyteam.entity.TeamMemberInfo;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.repository.TeamMemberInfoRepository;
import HelloMyTeam.Hellomyteam.repository.TeamRepository;
import HelloMyTeam.Hellomyteam.repository.custom.impl.TeamCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

import static HelloMyTeam.Hellomyteam.entity.QTeam.team;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamMemberInfoRepository teamMemberInfoRepository;
    private final TeamCustomImpl teamCustomImpl;
    private final TeamRepository teamRepository;

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("userId가 존재하지 않습니다."));
        if (member.getMemberStatus().equals(MemberStatus.WITHDRAW)) {
            return CommonResponse.createError("탈퇴한 회원입니다.");
        }
        if (member.getMemberStatus().equals(MemberStatus.EXIT)) {
            return CommonResponse.createError("관리자에 의해 강제탈퇴된 회원입니다.");
        }

        //authority
        HashMap<String, Object> hashMap = new HashMap<>();
        //user 가 가입한 teamMemberInfoIds 가져오기
        List<Long> findTeamMemberIds = teamCustomImpl.findTeamMemberInfoIdsByMemberId(memberId);
        log.info("@ findTeamMemberIds:" + findTeamMemberIds);

        for (Long teamMemberInfoId : findTeamMemberIds) {
            log.info("@ teamMemberInfoId:" + teamMemberInfoId);
            Team team = teamCustomImpl.findTeamByTeamMemberInfoId(teamMemberInfoId);
            hashMap.put("teamId", team.getId());
            hashMap.put("teamName", team.getTeamName());
        }

        //팀이름, 팀 id
//        List<TeamNameIdDto> team = teamCustomImpl.findTeamNameByIds(findTeamMemberIds);
//        List<Team> team = teamCustomImpl.findTeamByIds(findTeamMemberIds);
//        List<Team> team = teamMemberInfoRepository.findTeamByTeamMemberInfoId(findTeamMemberIds);

        //게시판 가져오기

        //팀원 정보

        //공지사항 2개 //자유게시판 5개 분리


//        List<TeamLoadResDto> joinedTeamInfo = teamCustomImpl.getTeamInfoByTeamMemberIds(findTeamMemberIds);

        return CommonResponse.createSuccess(hashMap);
    }
}
