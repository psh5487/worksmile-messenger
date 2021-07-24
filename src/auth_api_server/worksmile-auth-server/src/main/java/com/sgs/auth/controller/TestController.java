package com.sgs.auth.controller;

import com.sgs.auth.dto.ApiResult;
import com.sgs.auth.dto.user.UserDto;
import com.sgs.auth.model.User;
import com.sgs.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/test")
@Slf4j
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/server/hello")
    public String connectTest() {
        return "Hello! I am Auth Server";
    }

    @GetMapping("/myInfo")
    public ApiResult myInfo(@AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser) {

        if(loggedInUser == null) {
            return new ApiResult(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다.", null);
        }

        UserDto userDto = userService.findUserById(loggedInUser.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("user", userDto);

        return new ApiResult(HttpStatus.OK.value(), "사용자 정보", userDto);
    }

    @GetMapping("/users")
    public ApiResult allUsers() {
        List<UserDto> users = userService.allUserInfo();
        return new ApiResult(200, "All Users Test", users);
    }

    //This method will be used to check if the user has a valid token to access the resource
    @RequestMapping("/validateUser")
    public Principal user(Principal user) {
        return user;
    }
}
