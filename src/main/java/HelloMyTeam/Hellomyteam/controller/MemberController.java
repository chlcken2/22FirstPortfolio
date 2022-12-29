package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.exception.JwtTokenException;
import HelloMyTeam.Hellomyteam.payload.ApiResponse;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class MemberController {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final MemberRepository memberRepository;

//    public MemberController(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    @GetMapping("/user/me")
    public ApiResponse<?> getMyMemberInfo(HttpServletRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member savedMember = memberRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(savedMember)) {
            return ApiResponse.createError(new JwtTokenException(request.getHeader(AUTHORIZATION_HEADER)).getMessage());
        }

        return ApiResponse.createSuccess(savedMember);
    }
}
