package com.worksmile.user.controller;

import com.worksmile.user.dto.AllServiceSettingDto;
import com.worksmile.user.dto.ApiResult;
import com.worksmile.user.dto.CompanyDto;
import com.worksmile.user.service.CompanysService;
import com.worksmile.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test/user-server")
    public String ConnTest() {
        return "CONNECTED!!";
    }

    @PutMapping("/service-setting/user/{uid}")
    public ApiResult updatePrivateSetting(@PathVariable String uid,
                                          @RequestBody AllServiceSettingDto allServiceSettingDto) {
        log.info("updatePrivateSetting call : {}, {}", uid, allServiceSettingDto);
        AllServiceSettingDto allServiceSettingResult = userService.updateUserAllPushNotice(uid);
        ApiResult res = new ApiResult(200, "변경 성공", allServiceSettingResult);
        log.info("updatePrivateSetting return : {}", res);
        return res;
    }


}
