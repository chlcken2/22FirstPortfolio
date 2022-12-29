package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import HelloMyTeam.Hellomyteam.exception.BadRequestException;
import HelloMyTeam.Hellomyteam.exception.UserNotFoundException;
import HelloMyTeam.Hellomyteam.jwt.Token;
import HelloMyTeam.Hellomyteam.payload.ApiResponse;
import HelloMyTeam.Hellomyteam.payload.AuthResponse;
import HelloMyTeam.Hellomyteam.payload.LoginRequest;
import HelloMyTeam.Hellomyteam.payload.SignUpRequest;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        memberRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        Token token = tokenProvider.generateToken(email, "ROLE_USER");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if(memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        Member member = Member.builder()
                                .name(signUpRequest.getName())
                                .email(signUpRequest.getEmail())
                                .memberStatus(MemberStatus.NORMAL)
                                .build();
        memberRepository.save(member);

        ApiResponse apiResponse = new ApiResponse(true, "User registered successfully");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
