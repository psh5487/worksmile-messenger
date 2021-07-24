package com.sgs.auth.security;

import com.sgs.auth.model.User;
import com.sgs.auth.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    // 토큰 유효 시간
    public final static long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 10; // 10분
    public final static long REFRESH_TOKEN_VALID_TIME =  1000L * 60 * 60 * 24 * 2; // 2일

    public final static String ACCESS_TOKEN_NAME = "X-Auth-Token";

    // Secret Key
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key key;

    // 의존성 주입 이루어진 후 객체 초기화, secret 을 인코딩
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Request 의 Header 에서 token 값 가져오기 - "X-Auth-Token" : "TOKEN 값"
    public String resolveToken(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    // JWT 토큰 Claims Parsing 하기
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 토큰에서 UserPk 구하기
    public String getUserPk(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 사용자 인증 정보 조회 with JWT 토큰
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 사용자 인증 정보 조회 with UserPk
    public Authentication getAuthenticationWithUserPk(String userPk) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPk);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰 만료 여부 확인
    public Boolean isTokenAlive(String token) {
        Date expiration = extractAllClaims(token).get("expiration", Date.class);
        return new Date().before(expiration);
    }

    // JWT 토큰 유효성 확인 - userPk 조건 & Expiration Time 조건
    public Boolean validateToken(String token, UserDetails userDetails) {
        String userPk = getUserPk(token);
        return (userPk.equals(userDetails.getUsername()) && isTokenAlive(token));
    }

    // JWT 토큰 생성하기
    public String createToken(String userPk, String role, long expireTime) {

        // Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // Claim - Payload 에 저장되는 정보 단위
        Claims claims = Jwts.claims().setSubject(userPk);

        Date now = new Date();
        claims.put("expiration", new Date(now.getTime() + expireTime)); // Expire Time
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)  // 토큰 발행 시간 정보
                .signWith(key, SignatureAlgorithm.HS256) // signature 에 들어갈 secret 값 세팅
                .compact();
    }

    // Access 토큰 생성하기
    public String createAccessToken(User user) {
        return createToken(user.getUid(), user.getRole().toString(), ACCESS_TOKEN_VALID_TIME);
    }

    // Refresh 토큰 생성하기
    public String createRefreshToken(User user) {
        return createToken(user.getUid(), user.getRole().toString(), REFRESH_TOKEN_VALID_TIME);
    }
}
