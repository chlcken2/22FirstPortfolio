package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import HelloMyTeam.Hellomyteam.exception.BadRequestException;
import HelloMyTeam.Hellomyteam.exception.UserNotFoundException;
import HelloMyTeam.Hellomyteam.jwt.Token;
import HelloMyTeam.Hellomyteam.payload.ApiResponse;
import HelloMyTeam.Hellomyteam.payload.LoginRequest;
import HelloMyTeam.Hellomyteam.payload.SignUpRequest;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.service.TokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @ApiOperation(value = "login", notes = "로그인")
    @PostMapping("/login")
    public ApiResponse<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        if (ObjectUtils.isEmpty(memberRepository.findByEmail(email))) {
            return ApiResponse.createError(new UserNotFoundException(email).getMessage());
        }

        Token token = tokenProvider.generateToken(email, "ROLE_USER");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return ApiResponse.createSuccess(token);

//        return ResponseEntity.ok(token);
    }
    @ApiOperation(value = "signup", notes = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if(memberRepository.existsByEmail(signUpRequest.getEmail())) {
            return ApiResponse.createError(new BadRequestException("Email address already in use.").getMessage());
        }

        Member member = Member.builder()
                                .name(signUpRequest.getName())
                                .email(signUpRequest.getEmail())
                                .memberStatus(MemberStatus.NORMAL)
                                .build();
        memberRepository.save(member);

        return ApiResponse.createSuccess(member, "User registered successfully");
    }
}
