package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.dto.MemberDTO;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
//    @GetMapping("/user/me")
//    @PreAuthorize("hasRole('USER')")
//    public MemberDTO getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
//        MemberDTO memberDTO = new MemberDTO();
//
//        Member member = memberRepository.findById(userPrincipal.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
//
//        BeanUtils.copyProperties(member, memberDTO);
//
//        return memberDTO;
//    }


    @GetMapping("/user/me")
    public ResponseEntity<Member> getMyMemberInfo() {
        Optional<Member> savedMember = memberRepository.findById(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
        savedMember.map(Member::getEmail);
        return ResponseEntity.ok((savedMember.get()));
    }
}
