package likelion.tupl.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtSting = resolveToken(request);
        String requestURI = request.getRequestURI();

        if (StringUtils.hasText(jwtSting) && jwtProvider.validateToken(jwtSting)){
            Authentication authentication = jwtProvider.getAuthentication(jwtSting);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다. URI : {}", authentication.getName(), requestURI);
        }
        else{
            log.debug("유효한 JWT 토큰이 없습니다. URI : {}", requestURI);
        }
        filterChain.doFilter(request, response); // 다음 filter 호출
    }

    // Request Header 의 Authorization 속성에서 Bearer 뒤의 token 문자열만 가져오는 메소드
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
// StringUtils 클래스의 hasText 메소드는 유효성을 확인할 문자열을 인자로 받는다
// Returns : true if the String is not null, its length is greater than 0, and it does not contain whitespace only
// 즉, null 이 아니고, 길이가 0보다 크고, 공백으로만 이루어진 경우("  ")가 아닐 때 true 를 반환한다.