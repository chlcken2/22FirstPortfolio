package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.dto.AuthNumDto;
import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.entity.TermsAndCond;
import HelloMyTeam.Hellomyteam.entity.status.MemberStatus;
import HelloMyTeam.Hellomyteam.exception.BadRequestException;
import HelloMyTeam.Hellomyteam.exception.JwtTokenException;
import HelloMyTeam.Hellomyteam.exception.MemberNotFoundException;
import HelloMyTeam.Hellomyteam.jwt.Token;
import HelloMyTeam.Hellomyteam.dto.CommonResponse;
import HelloMyTeam.Hellomyteam.dto.MemberRequest;
import HelloMyTeam.Hellomyteam.dto.SignUpRequest;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import HelloMyTeam.Hellomyteam.service.AuthService;
import HelloMyTeam.Hellomyteam.service.TokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AuthService authService;

    @ApiOperation(value = "certification", notes="이메일에 인증번호 전송")
    @GetMapping("/email")
    public void mailCertification(@RequestParam String mail){
       // AuthService authService = new AuthService();
        authService.sendMail(mail);
    }

    @ApiOperation(value = "numMatching" , notes = "이메일에 전송된 난수 매칭")
    @GetMapping("/match")
    public void numMatching(@RequestParam int num){

        System.out.println(num);
        AuthNumDto authNumDto = authService.getAuthNumDto();

        String chkstr = authNumDto.getAuthCode();
        System.out.println(chkstr);
        System.out.println("테스트입니다.");

    }

    @ApiOperation(value = "login", notes = "로그인")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @PostMapping("/login")
    public CommonResponse<?> authenticateUser(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        String email = memberRequest.getEmail();
        Member savedMember = memberRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(savedMember)) {
            response.setStatus(404);
            return CommonResponse.createError(new MemberNotFoundException(email, null).getMessage());
        }

        boolean checkPw = passwordEncoder.matches(memberRequest.getPassword(), savedMember.getPassword());

        if (!checkPw) {
            response.setStatus(404);
            return CommonResponse.createError(new MemberNotFoundException("password do not match").getMessage());
        }

        Token token = tokenProvider.generateToken(email, "ROLE_USER");

        savedMember.setRefreshToken(token.getRefreshToken());
        memberRepository.save(savedMember);

        return CommonResponse.createSuccess(token);
    }

    @ApiOperation(value = "signup", notes = "회원가입")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @PostMapping("/signup")
    public CommonResponse<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if(memberRepository.existsByEmail(signUpRequest.getEmail())) {
            return CommonResponse.createError(new BadRequestException("Email address already in use.").getMessage());
        }

        String encodePassword = passwordEncoder.encode(signUpRequest.getPassword());

        Member member = Member.builder()
                                .email(signUpRequest.getEmail())
                                .password(encodePassword)
                                .name(signUpRequest.getName())
                                .birthday(signUpRequest.getBirthday())
                                .joinPurpose(signUpRequest.getJoinPurpose())
                                .termsAndCond(new ArrayList<>())
                                .memberStatus(MemberStatus.NORMAL)
                                .build();

        member.addTermsAndCond(TermsAndCond.builder()
                .termsOfServiceYn(signUpRequest.getTermsOfServiceYn())
                .privacyYn(signUpRequest.getPrivacyYn())
                .build());

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
