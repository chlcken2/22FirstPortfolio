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
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final AuthService authService;

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     *
     * @param mail 인증번호를 받을 이메일
     * @return 인증번호와 이메일 담은 토큰
     */
    @ApiOperation(value = "certification", notes="이메일에 인증번호 전송 인증번호를 담음 토큰 리턴")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @GetMapping("/email")
    public String mailCertification(@RequestParam String mail){
       // AuthService authService = new AuthService();
        String token = authService.sendMail(mail);
        return token;
    }

    @ApiOperation(value = "numMatching" , notes = "이메일에 전송된 난수 매칭")
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @GetMapping("/match")
    public CommonResponse<?> numMatching(@RequestParam int num){

        AuthNumDto authNumDto = authService.getAuthNumDto();
        System.out.println(authNumDto.getAuthNumber());
        if (authNumDto == null) {
            System.out.println("인증번호를 입력해주세요.");
            return CommonResponse.createError("인증번호를 입력해주세요.");
        }

        if (num == authNumDto.getAuthNumber()) {
            System.out.println("인증번호가 일치합니다.");
            return CommonResponse.createSuccess("인증번호가 일치합니다.");
        } else {
            System.out.println("인증번호가 일치하지 않습니다.");
            return CommonResponse.createError("인증번호가 일치하지 않습니다.");
        }
    }
    @ApiImplicitParam(name = "Authorization", value = "Access Token 입력x")
    @GetMapping("/verify")
    public void verify(@RequestParam String auth, @RequestParam int authNumber){
        try{
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(auth);

            String email = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(auth).getBody().getSubject();
            int AuthNum = (int) Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(auth).getBody().get("authNumber");

            if(AuthNum == authNumber){
                System.out.println("인증성공");
            }else{
                System.out.println("인증 실패");
            }

        }catch (Exception e){
            System.out.println("토큰 검증 실패");
            System.out.println(e);
        }
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
}
