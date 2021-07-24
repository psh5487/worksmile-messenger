package com.sgs.auth.controller;

import com.sgs.auth.dto.ApiResult;
import com.sgs.auth.security.JwtTokenProvider;
import com.sgs.auth.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/token")
@Slf4j
public class IssueTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public IssueTokenController(JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }

    @PostMapping("/issue")
    public ApiResult issueNewToken(@RequestBody Map<String, String> request) {

        String expiredToken = request.get("expired_token");

        // Access Token 에서 UserPK(Email), Role 추출 (DB 접근 안 하고, 기존 데이터 재활용 하는 방식)
        Claims claims = jwtTokenProvider.extractAllClaims(expiredToken);

        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        // Redis 에서 Refresh Token 확인
        String refreshTokenInRedis = redisUtil.getData(userId);

        if(refreshTokenInRedis != null && userId.equals(jwtTokenProvider.getUserPk(refreshTokenInRedis))) {
            log.info("Refresh token is valid");

            // Access Token 재발급
            String newJwtToken = jwtTokenProvider.createToken(userId, role, JwtTokenProvider.ACCESS_TOKEN_VALID_TIME);

            Map <String, String> map = new HashMap<>();
            map.put("new_token", newJwtToken);

            return new ApiResult(HttpStatus.OK.value(), "새 토큰 발급", map);
        } else {
            return new ApiResult(HttpStatus.UNAUTHORIZED.value(), "인증 만료", null);
        }
    }
}
