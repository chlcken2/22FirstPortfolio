package hellomyteam.hellomyteam.api;

import hellomyteam.hellomyteam.dto.AuthNumDto;
import hellomyteam.hellomyteam.entity.Member;
import hellomyteam.hellomyteam.entity.TermsAndCond;
import hellomyteam.hellomyteam.entity.status.MemberStatus;
import hellomyteam.hellomyteam.exception.BadRequestException;
import hellomyteam.hellomyteam.exception.JwtTokenException;
import hellomyteam.hellomyteam.exception.MemberNotFoundException;
import hellomyteam.hellomyteam.jwt.Token;
import hellomyteam.hellomyteam.dto.CommonResponse;
import hellomyteam.hellomyteam.dto.MemberRequest;
import hellomyteam.hellomyteam.dto.SignUpRequest;
import hellomyteam.hellomyteam.repository.MemberRepository;
import hellomyteam.hellomyteam.service.AuthService;
import hellomyteam.hellomyteam.service.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;


    /**
     *
     * @param mail 인증번호를 받을 이메일
     * @return 인증번호와 이메일 담은 토큰
     */
    @ApiOperation(value = "certification", notes="이메일에 인증번호 전송 인증번호를 담음 토큰 리턴")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @GetMapping("/email")
    public String mailCertification(@RequestParam String mail){
        String token = authService.sendMail(mail);
        return token;
    }

    @ApiOperation(value = "tokenVerify", notes = "이메일에 전송된 인증번호와 해당 아이디로 발급된 토큰 내 인증번호 비교")
    @ApiImplicitParams({
            @ApiImplicitParam(name="auth" , value = "토큰 입력"),
            @ApiImplicitParam(name="authNumber", value = "인증번호")
    })
    @GetMapping("/verify")
    public CommonResponse<?> verify(@RequestParam String auth, @RequestParam int authNumber, HttpServletResponse response){
        return authService.chkJWT(auth, authNumber);
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
        String email = signUpRequest.getEmail().replaceAll("(\r\n|\r|\n|\n\r|\\p{Z}|\\t)", "");

        CommonResponse<?> response = isValidEmail(email);

        if(!"올바른 이메일 형식입니다.".equals(response.getData())){
            return CommonResponse.createError(response.getMessage());
        }

        if(memberRepository.existsByEmail(email)) {
            return CommonResponse.createError(new BadRequestException("Email address already in use.").getMessage());
        }

        String encodePassword = passwordEncoder.encode(signUpRequest.getPassword());

        Member member = Member.builder()
                                .email(email)
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
    @GetMapping("/refresh")
    public CommonResponse<?> readCookie(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorizationHeader)) {
            authorizationHeader = authorizationHeader.substring(7);
        }

        Member member = memberRepository.findByEmail(tokenProvider.getUid(authorizationHeader));
        if (member.getRefreshToken().equals(authorizationHeader)) {
            switch (response.getStatus()) {
                case 200: {
                    Token token = tokenProvider.generateToken(member.getEmail(), "ROLE_USER");
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

        return CommonResponse.createError("refresh Token 같지않음");
    }

    public static CommonResponse<?> isValidEmail (String email){
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        if("".equals(email))
            return CommonResponse.createError("이메일을 작성해주세요.");
        if(!pattern.matcher(email).matches())
            return CommonResponse.createError("올바른 이메일 형식이 아닙니다.");

        return CommonResponse.createSuccess("올바른 이메일 형식입니다.");
    }
}
