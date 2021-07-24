package com.sgs.auth.controller;

import com.sgs.auth.dto.ApiResult;

import com.sgs.auth.model.User;
import com.sgs.auth.service.AuthService;
import com.sgs.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/help")
@Slf4j
public class HelpController {

    private final AuthService authService;

    public HelpController(AuthService authService, UserService userService) {
        this.authService = authService;
    }

    @PostMapping("/find/id")
    public ApiResult findUserId(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            User user = authService.findUserByEmail(email);

            Map<String, Object> map = new HashMap<>();
            map.put("uid", user.getUid());

            return new ApiResult(200, "아이디 찾기 성공", map);

        } catch (RuntimeException e) {
            return new ApiResult(400, e.getMessage(), null);
        }
    }

    @PostMapping("/pwd/email")
    public ApiResult sendChangePwdLinkEmail(@RequestBody Map<String, String> request) {
        String userId = request.get("uid");

        try {
            User user = authService.sendVerificationEmail(userId);

            Map<String, Object> map = new HashMap<>();
            map.put("uid", user.getUid());

            return new ApiResult(200, "비밀번호 변경 인증 이메일 보내기 성공", map);

        } catch (RuntimeException e) {
            return new ApiResult(400, "비밀번호 변경 인증 이메일 보내기 실패", null);
        }
    }

    @GetMapping("/pwd/{verificationKey}")
    public String getVerify(@PathVariable String verificationKey) {

        try {
            String resetPwd = authService.verifyEmail(verificationKey);
            return "초기화된 비밀번호 : " + resetPwd;

        } catch (Exception e) {
            return "인증 시간이 만료되었습니다.";
        }
    }
}
