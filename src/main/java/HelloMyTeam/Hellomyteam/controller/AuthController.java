package HelloMyTeam.Hellomyteam.controller;

import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import HelloMyTeam.Hellomyteam.exception.BadRequestException;
import HelloMyTeam.Hellomyteam.exception.JwtTokenException;
import HelloMyTeam.Hellomyteam.exception.UserNotFoundException;
import HelloMyTeam.Hellomyteam.jwt.Token;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.payload.MemberRequest;
import HelloMyTeam.Hellomyteam.payload.SignUpRequest;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.service.TokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @ApiOperation(value = "login", notes = "로그인")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @PostMapping("/login")
    public CommonResponse<?> authenticateUser(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        String email = memberRequest.getEmail();
        Member savedMember = memberRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(savedMember)) {
            response.setStatus(404);
            return CommonResponse.createError(new UserNotFoundException(email).getMessage());
        }

        Token token = tokenProvider.generateToken(email, "ROLE_USER");

        savedMember.setRefreshToken(token.getRefreshToken());
        memberRepository.save(savedMember);

//        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
//                .maxAge(60 * 60 * 24 * 30)   // 쿠키 유효기간 30일
//                .path("/")
//                .secure(true)
//                .sameSite("None")
//                .httpOnly(true)
//                .build();
//        response.setHeader("Set-Cookie", cookie.toString());

        return CommonResponse.createSuccess(token);
    }

    @ApiOperation(value = "signup", notes = "회원가입")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @PostMapping("/signup")
    public CommonResponse<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if(memberRepository.existsByEmail(signUpRequest.getEmail())) {
            return CommonResponse.createError(new BadRequestException("Email address already in use.").getMessage());
        }

        Member member = Member.builder()
                                .name(signUpRequest.getName())
                                .email(signUpRequest.getEmail())
                                .memberStatus(MemberStatus.NORMAL)
                                .build();
        memberRepository.save(member);

        return CommonResponse.createSuccess(member, "User registered successfully");
    }

    @ApiOperation(value = "refresh", notes = "accessToken 만료 시 refresh 토큰으로 jwt 토큰 재생성")
    @ApiImplicitParam(name = "Authorization", value = "Refresh Token(접두사: Bearer) 입력 필요")
    @ApiResponses({
            @ApiResponse(code=496, message="잘못된 JWT 서명")
            , @ApiResponse(code=497, message="지원되지 않는 JWT 토큰")
            , @ApiResponse(code=498, message="만료된 JWT 토큰")
            , @ApiResponse(code=499, message="JWT 토큰 형식 오류")
            , @ApiResponse(code=490, message="refresh Token 같지 않음 > 로그인 재요청")
    })
    @PostMapping("/refresh")
    public CommonResponse<?> readCookie(@RequestBody MemberRequest memberRequest, HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorizationHeader)) {
            authorizationHeader = authorizationHeader.substring(7);
        }

        Member member = memberRepository.findByEmail(memberRequest.getEmail());
        if (StringUtils.hasText(member.getEmail())) {
            if (member.getRefreshToken().equals(authorizationHeader)) {

                switch (response.getStatus()) {
                    case 200: {
                        Token token = tokenProvider.generateToken(memberRequest.getEmail(), "ROLE_USER");
                        member.setRefreshToken(token.getRefreshToken());
                        memberRepository.save(member);
                        return CommonResponse.createSuccess(token);
                    }
                    case 496: return CommonResponse.createError(new JwtTokenException("MalformedJwtException").getMessage());
                    case 497: return CommonResponse.createError(new JwtTokenException("UnsupportedJwtException").getMessage());
                    case 498: return CommonResponse.createError(new JwtTokenException("ExpiredJwtException").getMessage());
                    case 499: return CommonResponse.createError(new JwtTokenException("Exception").getMessage());
                }
            } else {
                response.setStatus(490);
            }
        }

        return CommonResponse.createError("refresh Token 같지않음");
    }
}
