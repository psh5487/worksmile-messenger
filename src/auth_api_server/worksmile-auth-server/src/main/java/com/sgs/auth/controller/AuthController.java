package com.sgs.auth.controller;

import com.sgs.auth.dto.ApiResult;
import com.sgs.auth.dto.user.AuthenticationRequest;
import com.sgs.auth.dto.user.JoinRequest;
import com.sgs.auth.dto.user.JoinResult;
import com.sgs.auth.dto.user.UserDto;
import com.sgs.auth.model.User;
import com.sgs.auth.security.JwtTokenProvider;
import com.sgs.auth.service.AuthService;
import com.sgs.auth.service.UserService;
import com.sgs.auth.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public AuthController(AuthService authService, UserService userService, JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }

    @PostMapping("/join")
    public ApiResult join(@RequestBody JoinRequest joinRequest) {

        try { // try catch 범위 확실하게 잡기 // 바로 map 을 리턴하도록
            User user = authService.join(joinRequest);

            JoinResult joinResult = JoinResult.builder()
                    .uid(user.getUid())
                    .uname(user.getUname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .subroot_cid(joinRequest.getSubroot_cid())
                    .subroot_cname(joinRequest.getSubroot_cname())
                    .build();

            Map<String, Object> map = new HashMap<>();
            map.put("join_result", joinResult);

            return new ApiResult(200, "회원가입이 요청되었습니다.", map);

        } catch (RuntimeException e) { // 요청 시 잘못된 인자값 예외 or 중복 사용자 or 기타
            return new ApiResult(400, e.getMessage(), null);
        }
    }

    @PostMapping("/id/unique")
    public ApiResult isUniqueId(@RequestBody Map<String, String> request) {
        String userId = request.get("uid");
        Boolean isUniqueId = authService.isUniqueId(userId);

        Integer status = 200;
        String message = "사용 가능한 아이디입니다.";

        if(!isUniqueId) {
            message = "이미 등록된 아이디입니다.";
            status = HttpStatus.CONFLICT.value();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uid", userId);
        map.put("is_unique", isUniqueId);

        return new ApiResult(status, message, map);
    }

    @PostMapping("/join/email")
    public ApiResult sendRegisterFinishEmail(@RequestBody Map<String, String> request) {
        String registerType = request.get("type");
        String userId = request.get("uid");

        try {
            User user = authService.sendRegisterFinishEmail(registerType, userId);
            Map<String, Object> map = new HashMap<>();
            map.put("uid", userId);
            return new ApiResult(200, "가입 승인 완료 이메일 보내기 성공", map);

        } catch (RuntimeException e) {
            return new ApiResult(400, "가입 승인 완료 이메일 보내기 실패", null);
        }
    }

    @PostMapping("/login")
    public ApiResult login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {

        try {
            User user = authService.login(request.getUid(), request.getPwd());
            UserDto userDto = userService.makeUserDto(user);

            // Access Token 과 Refresh Token 발행
            String accessToken = jwtTokenProvider.createAccessToken(user);
            String refreshToken = jwtTokenProvider.createRefreshToken(user);

            // Redis 에 Refresh Token 저장
            redisUtil.setDataExpire(user.getUid(), refreshToken, redisUtil.REFRESH_TOKEN_REDIS_DURATION);

            // 헤더에 토큰 넣어주기
            response.setHeader(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken);

            Map<String, Object> map = new HashMap<>();
            map.put("user", userDto);
            map.put(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken);

            return new ApiResult(200, "로그인 성공", map);

        } catch (RuntimeException e) { // 가입되지 않은 사용자 or 비밀번호 틀림 or 요청 시 잘못된 인자 값
            return new ApiResult(400, e.getMessage(), null);
        }
    }

    @PostMapping("/logout")
    public ApiResult logout(@RequestBody Map<String, String> request) {

        String userId = request.get("uid");

        redisUtil.deleteData(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("uid", userId);

        return new ApiResult(200, "로그아웃 성공", map);
    }

    @DeleteMapping("/{uid}")
    public ApiResult leave(@PathVariable String uid) {
        System.out.println(uid);

        try {
            authService.leave(uid);

            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);
            return new ApiResult(200, "회원 탈퇴 성공", map);

        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return new ApiResult(400, "회원 탈퇴 실패", null);
        }
    }
}
