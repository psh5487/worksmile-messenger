package com.sgs.auth.security;

import com.sgs.auth.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws IOException, ServletException {

        // 헤더에서 Access Token(JWT Token) 받아오기
        String jwtToken = jwtTokenProvider.resolveToken(request, jwtTokenProvider.ACCESS_TOKEN_NAME);

        if (jwtToken != null) {
            // 유효한 Access Token 인지 확인
            if(jwtTokenProvider.isTokenAlive(jwtToken)) { // 1. Access Token 이 유효한 경우
                log.info("Access token is valid");

                // 토큰으로부터 유저 정보를 받아옴
                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);

                // SecurityContext 에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else { // 2. Access Token 이 만료된 경우
                log.info("Access token is invalid");

                // Access Token 에서 UserPK(Email), Role 추출 (DB 접근 안 하고, 기존 데이터 재활용 하는 방식)
                Claims claims = jwtTokenProvider.extractAllClaims(jwtToken);

                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                // Redis 에서 Refresh Token 확인
                String refreshTokenInRedis = redisUtil.getData(userId);

                // 2-1. Refresh Token 이 유효할 경우 - Redis 에 존재 & (사용자가 갖고 있던 토큰의 UserPk 와 Redis 에 저장된 토큰의 UserPk가 같아야함)
                if(refreshTokenInRedis != null && userId.equals(jwtTokenProvider.getUserPk(refreshTokenInRedis))) {
                    log.info("Refresh token is valid");

                    // Access Token 재발급
                    jwtToken = jwtTokenProvider.createToken(userId, role, JwtTokenProvider.ACCESS_TOKEN_VALID_TIME);

                    // 유저 정보를 받아옴
                    Authentication authentication = jwtTokenProvider.getAuthenticationWithUserPk(userId);

                    // SecurityContext 에 Authentication 객체 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 헤더에 Access Token 넣어주기
        response.setHeader(jwtTokenProvider.ACCESS_TOKEN_NAME, jwtToken);

        // Access Token, Refresh Token 둘 다 유효하지 않을 경우, 다시 로그인 해야함

        filterchain.doFilter(request, response);
    }
}
