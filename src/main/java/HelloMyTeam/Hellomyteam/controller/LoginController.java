package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.dto.MemberDTO;
import HelloMyTeam.Hellomyteam.jwt.JwtTokenProvider;
import HelloMyTeam.Hellomyteam.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    private final HttpSession httpSession;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        MemberDTO member = (MemberDTO) httpSession.getAttribute("user");
        if (memberService.isMember(member.getEmail())) {

        }
        return "redirect:/signup";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
