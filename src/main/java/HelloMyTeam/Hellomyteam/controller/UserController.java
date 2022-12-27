package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.dto.MemberDTO;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.exception.ResourceNotFoundException;
import HelloMyTeam.Hellomyteam.repository.UserRepository;
import HelloMyTeam.Hellomyteam.security.CurrentUser;
import HelloMyTeam.Hellomyteam.security.UserPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public MemberDTO getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        MemberDTO memberDTO = new MemberDTO();

        Member member = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        BeanUtils.copyProperties(member, memberDTO);

        return memberDTO;
    }
}
