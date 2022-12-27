package HelloMyTeam.Hellomyteam.service.impl;

import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public boolean isMember(String email) {
        return !ObjectUtils.isEmpty(memberRepository.findByEmail(email));
    }
}
