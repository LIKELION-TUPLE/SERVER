package likelion.tupl.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "auth";

    // private final String secret;
    private final Long jwtValidityInMilliseconds; // JWT 유효 기간

    // applicaiton.yml 에서 jwt.secret 에 비밀키 문자열 드러내지 않아도 됨 -> 자동 생성
    // HS512 알고리즘의 최소 길이 기준을 만족하는 임의의 비밀키 값 생성해줌 (HS512 는 64bit)
    // secretKey 에는 64비트 바이트 배열 존재
    private final SecretKey secretKey = Jwts.SIG.HS512.key().build();
    // private final String secretString = Encoders.BASE64.encode(secretKey.getEncoded());

    public JwtProvider(
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds) {
        this.jwtValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
    }

    // 토큰 생성
    public String createToken(Authentication authentication){
        /*String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toString();*/
        String authorities = authentication.getAuthorities().stream() // 현재 사용자에게 할당된 권한 목록을 반환 후 stream 으로 변환
                // 이 목록은 GrantedAuthority 객체들의 컬렉션으로 되어있다
                .map(GrantedAuthority::getAuthority) // 스트림의 각 요소를 처리하여 GrantedAuthority 객체에서 getAuthority() 메소드 호출
                // 이 메소드는 권한 이름을 문자열로 반환하는 메소드이다
                .collect(Collectors.joining(",")); // 스트림 요소를 쉼표로 구분된 하나의 문자열로 결합

        Date now = new Date();
        Date validity = new Date(now.getTime() + this.jwtValidityInMilliseconds); // jwt 만료 날짜 및 시간 정보

        return Jwts.builder()
                .subject(authentication.getName()) // Claim "sub"
                .expiration(validity) // Claim "exp"
                .claim(AUTHORITIES_KEY, authorities) // Claim "auth"
                .signWith(secretKey, Jwts.SIG.HS512) // secretKey 를 가지고 HMAC SHA-512 알고리즘을 통해 Signature 생성
                .compact(); // JWT 문자열로 압축 후 return
    }
    // 토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            // 문자열 타입의 JWS -> Jwts 타입의 JWS 변환
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){ // multi catch
            log.info("잘못된 JWT 서명입니다.");
        } catch(ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        } catch(UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch(IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 인자로 받은 jws 문자열에 문제가 있으면 false return
    }

    // token 에 담겨 있는 정보를 통해 Authentication 객체 생성 후 반환
    public Authentication getAuthentication(String token){

        // Claims := Map<String, Objects> 상속받는 interface
        Claims claims = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // authorities 에는 ROLE_STUDENT 또는 ROLE_TEACHER
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(claims.get(AUTHORITIES_KEY).toString()));

        /*Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(" "))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());*/

        // principal := 보호받는 자원에 접근하는 대상
        User principal = new User(claims.getSubject(), "", grantedAuthorities);

        return new UsernamePasswordAuthenticationToken(principal, token, grantedAuthorities);
    }
}
// AccessDeniedHandler : 서버에 요청을 할 때 액세스가 가능한지 권한 체크 후 액세스할 수 없는 요청을 했을 시 동작됨
// AuthenticationEntryPoint : 인증이 되지 않은 유저가 요청을 했을 때 동작
