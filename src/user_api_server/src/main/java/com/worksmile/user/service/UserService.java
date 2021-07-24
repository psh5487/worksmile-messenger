package com.worksmile.user.service;

import com.worksmile.user.domain.entity.WorksmileUsers;
import com.worksmile.user.domain.repository.WorksmileUsersRepo;
import com.worksmile.user.dto.AllServiceSettingDto;
import com.worksmile.user.dto.ApiResult;
import com.worksmile.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private WorksmileUsersRepo worksmileUsersRepo;

    public UserService(WorksmileUsersRepo worksmileUsersRepo) {
        this.worksmileUsersRepo = worksmileUsersRepo;
    }

    /**
     *  사용자 전체 설정
     */
    public AllServiceSettingDto updateUserAllPushNotice(String uid) {
        log.info("updateUserAllPushNotice call : {}", uid);
        AllServiceSettingDto allServiceSettingDto = new AllServiceSettingDto();

        // SELECT
        WorksmileUsers worksmileUsers = worksmileUsersRepo.findByUid(uid);
        if(worksmileUsers == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        } else {
            worksmileUsers.toggleAllPushNotice();
            worksmileUsersRepo.save(worksmileUsers);
        }
        allServiceSettingDto.setAllPushNotice(worksmileUsers.getAllPushNotice());
        log.info("updateUserAllPushNotice return : {}", allServiceSettingDto);
        return allServiceSettingDto;
    }
}
