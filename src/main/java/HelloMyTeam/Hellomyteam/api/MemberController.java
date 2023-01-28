package HelloMyTeam.Hellomyteam.api;

import HelloMyTeam.Hellomyteam.entity.Member;
import HelloMyTeam.Hellomyteam.exception.JwtTokenException;
import HelloMyTeam.Hellomyteam.exception.MemberNotFoundException;
import HelloMyTeam.Hellomyteam.payload.CommonResponse;
import HelloMyTeam.Hellomyteam.repository.MemberRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberRepository memberRepository;

    @ApiOperation(value = "user/me", notes = "Access Token 통한 본인 확인")
    @ApiImplicitParam(name = "Authorization", value = "Access Token(접두사: Bearer) 입력 필요")
    @ApiResponses({
            @ApiResponse(code=496, message="잘못된 JWT 서명")
            , @ApiResponse(code=497, message="지원되지 않는 JWT 토큰")
            , @ApiResponse(code=498, message="만료된 JWT 토큰")
            , @ApiResponse(code=499, message="JWT 토큰 형식 오류")
    })
    @GetMapping("/user/me")
    public CommonResponse<?> getMyMemberInfo(HttpServletResponse response) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member savedMember = memberRepository.findByEmail(email);

        if ("anonymousUser".equals(email)) {
            switch (response.getStatus()) {
                case 496: return CommonResponse.createError(new JwtTokenException("MalformedJwtException").getMessage());
                case 497: return CommonResponse.createError(new JwtTokenException("UnsupportedJwtException").getMessage());
                case 498: return CommonResponse.createError(new JwtTokenException("ExpiredJwtException").getMessage());
                case 499: return CommonResponse.createError(new JwtTokenException("Exception").getMessage());
            }
        } else if (ObjectUtils.isEmpty(savedMember)) {
            return CommonResponse.createError(new MemberNotFoundException(email, null).getMessage());
        }

        return CommonResponse.createSuccess(savedMember);
    }
}
