package com.worksmile.user.controller;

import com.worksmile.user.dto.ApiResult;
import com.worksmile.user.dto.AdminEditUserRequest;
import com.worksmile.user.dto.ForbiddenWordDto;
import com.worksmile.user.dto.UserDto;
import com.worksmile.user.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/super/users/company-admins")
    public ApiResult getAllCompanyAdmin() {
        log.info("getAllCompanyAdmin call");
        List<UserDto> companyAdmins = adminService.findAllCompanyAdmin();
        ApiResult res = new ApiResult(200, "admin user 추출", companyAdmins);
        log.info("getAllCompanyAdmin return : {}", res);
        return res;
    }

    @GetMapping("/company/users/company/{subrootCid}")
    public ApiResult getAllUserInSubrootCid(@PathVariable int subrootCid) {
        log.info("getAllUserInSubrootCid call : {}", subrootCid);
        List<UserDto> users = adminService.findAllUserInSubrootCid(subrootCid);
        ApiResult res = new ApiResult(200, "users 추출", users);
        log.info("getAllUserInSubrootCid return : {}", res);
        return res;
    }

    @GetMapping("/root-company/users/company/{RootCid}")
    public ApiResult getAllUserInRootCid(@PathVariable int RootCid) {
        log.info("getAllUserInRootCid call : {}", RootCid);
        List<UserDto> users = adminService.findAllUserInRootCidCid(RootCid);
        ApiResult res = new ApiResult(200, "users 추출", users);
        log.info("getAllUserInRootCid return : {}", res);
        return res;
    }

    @PutMapping("/user")
    public ApiResult updateUserByAdmin(@RequestBody AdminEditUserRequest adminEditUserRequest) {
        log.info("updateUserByAdmin call : {}", adminEditUserRequest);
        UserDto userDto = adminService.updateUserByAdmin(adminEditUserRequest);
        ApiResult res = new ApiResult(200, "유저 정보 변경 성공", userDto);
        log.info("updateUserByAdmin return : {}", res);
        return res;
    }

    @DeleteMapping("/user")
    public ApiResult deleteUserByAdmin(@RequestBody AdminEditUserRequest adminEditUserRequest) {
        log.info("deleteUserByAdmin call : {}", adminEditUserRequest);
        boolean isDelete = adminService.deleteUserByAdmin(adminEditUserRequest.getUid());
        ApiResult res = null;

        if(isDelete) {
            // 정상 삭제
            res = new ApiResult(200, "유저 삭제 성공", isDelete);
        } else {
            res = new ApiResult(400, "유저 삭제 실패", isDelete);
        }
        log.info("deleteUserByAdmin return : {}", res);
        return res;
    }

    @PostMapping("/super/forbidden-words")
    public ApiResult insertAndfindAllForbiddenWords(@RequestBody ForbiddenWordDto forbiddenWordDto) {
        log.info("insertAndfindAllForbiddenWords call : {}", forbiddenWordDto);

        List<ForbiddenWordDto> list = adminService.findAllForbiddenWords(forbiddenWordDto, 1);
        ApiResult res = new ApiResult(200, "비속어 추가 후 조회", list);

        log.info("insertAndfindAllForbiddenWords return : {}", res);
        return res;
    }

    @DeleteMapping("/super/forbidden-words")
    public ApiResult deleteAndfindAllForbiddenWords(@RequestBody ForbiddenWordDto forbiddenWordDto) {
        log.info("deleteAndfindAllForbiddenWords call : {}", forbiddenWordDto);

        List<ForbiddenWordDto> list = adminService.findAllForbiddenWords(forbiddenWordDto, 2);
        ApiResult res = new ApiResult(200, "비속어 삭제 후 조회", list);

        log.info("deleteAndfindAllForbiddenWords return : {}", res);
        return res;
    }
}
