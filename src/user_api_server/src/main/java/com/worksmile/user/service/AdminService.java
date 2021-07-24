package com.worksmile.user.service;

import com.worksmile.user.domain.entity.Companys;
import com.worksmile.user.domain.entity.ForbiddenWords;
import com.worksmile.user.domain.entity.Positions;
import com.worksmile.user.domain.entity.WorksmileUsers;
import com.worksmile.user.domain.repository.CompanysRepo;
import com.worksmile.user.domain.repository.ForbiddenWordsRepo;
import com.worksmile.user.domain.repository.PositionsRepo;
import com.worksmile.user.domain.repository.WorksmileUsersRepo;
import com.worksmile.user.dto.AdminEditUserRequest;
import com.worksmile.user.dto.ForbiddenWordDto;
import com.worksmile.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminService {
    public final static String COMPANY_ADMIN = "ROLE_COMPANY_ADMIN";
    public final static String ROOT_COMPANY_ADMIN = "ROLE_ROOT_COMPANY_ADMIN";
    public final static String NOT_PERMITTED_ADMIN = "ROLE_NOT_PERMITTED_ADMIN";

    private WorksmileUsersRepo worksmileUsersRepo;
    private CompanysRepo companysRepo;
    private PositionsRepo positionsRepo;
    private ForbiddenWordsRepo forbiddenWordsRepo;

    @Autowired
    public AdminService(WorksmileUsersRepo worksmileUsersRepo,
                        CompanysRepo companysRepo,
                        PositionsRepo positionsRepo,
                        ForbiddenWordsRepo forbiddenWordsRepo) {
        this.worksmileUsersRepo = worksmileUsersRepo;
        this.companysRepo = companysRepo;
        this.positionsRepo = positionsRepo;
        this.forbiddenWordsRepo = forbiddenWordsRepo;
    }

    /**
     *  Company Admin + Root Company Admin + NotPermitted Admin
     *  사용자들 가져오기
     */
    public List<UserDto> findAllCompanyAdmin() {
        log.info("getAllCompanyAdmin call");
        List<UserDto> res = new ArrayList<>();
        UserDto userDto = new UserDto();

        // SELECT ROLE_COMPANY_ADMIN + ROLE_ROOT_COMPANY_ADMIN
        List<WorksmileUsers> entityList
                = worksmileUsersRepo.findAllByRoleOrRoleOrRole(COMPANY_ADMIN, ROOT_COMPANY_ADMIN, NOT_PERMITTED_ADMIN);
        for(WorksmileUsers w : entityList) {
            userDto = UserDto.builder()
                    .uuid(null)
                    .uid(w.getUid())
                    .pwd(null)
                    .salt(null)
                    .uname(w.getUname())
                    .profile(w.getProfile())
                    .role(w.getRole())
                    .cid(w.getCid().getCid())
                    .cname(w.getCid().getCname())
                    .subrootCid(w.getSubrootCid().getCid())
                    .subrootCname(w.getSubrootCid().getCname())
                    .rootCid(w.getRootCid().getCid())
                    .rootCname(w.getRootCid().getCname())
                    .pid(w.getPid().getPid())
                    .pname(w.getPid().getPname())
                    .email(w.getEmail())
                    .phone(w.getPhone())
                    .registerAt(w.getRegisterAt())
                    .loginAt(w.getLoginAt())
                    .allPushNotice(w.getAllPushNotice())
                    .build();
            res.add(userDto);
        }
        log.info("getAllCompanyAdmin return : {}", res);
        return res;
    }

    /**
     *  해당 subrootCid 내 모든 사용자들 가져오기
     */
    public List<UserDto> findAllUserInSubrootCid(int subrootCid) {
        log.info("getAllUserInSubrootCid call : {}", subrootCid);
        List<UserDto> res = new ArrayList<>();
        UserDto userDto = new UserDto();

        Companys companys = companysRepo.findCompanyByCid(subrootCid);

        List<WorksmileUsers> entityList = worksmileUsersRepo.findAllBySubrootCid(companys);
        for(WorksmileUsers w : entityList) {
            userDto = UserDto.builder()
                    .uuid(null)
                    .uid(w.getUid())
                    .pwd(null)
                    .salt(null)
                    .uname(w.getUname())
                    .profile(w.getProfile())
                    .role(w.getRole())
                    .cid(w.getCid().getCid())
                    .cname(w.getCid().getCname())
                    .subrootCid(w.getSubrootCid().getCid())
                    .subrootCname(w.getSubrootCid().getCname())
                    .rootCid(w.getRootCid().getCid())
                    .rootCname(w.getRootCid().getCname())
                    .pid(w.getPid().getPid())
                    .pname(w.getPid().getPname())
                    .email(w.getEmail())
                    .phone(w.getPhone())
                    .registerAt(w.getRegisterAt())
                    .loginAt(w.getLoginAt())
                    .allPushNotice(w.getAllPushNotice())
                    .build();
            res.add(userDto);
        }
        log.info("getAllUserInSubrootCid return : {}", res);
        return res;
    }

    /**
     *  해당 rootCid 내 모든 사용자들 가져오기
     */
    public List<UserDto> findAllUserInRootCidCid(int cid) {
        log.info("getAllUserInRootCidCid call : {}", cid);
        List<UserDto> res = new ArrayList<>();
        UserDto userDto = new UserDto();

        Companys companys = companysRepo.findCompanyByCid(cid);

        List<WorksmileUsers> entityList = worksmileUsersRepo.findAllByRootCid(companys);
        for(WorksmileUsers w : entityList) {
            userDto = UserDto.builder()
                    .uuid(null)
                    .uid(w.getUid())
                    .pwd(null)
                    .salt(null)
                    .uname(w.getUname())
                    .profile(w.getProfile())
                    .role(w.getRole())
                    .cid(w.getCid().getCid())
                    .cname(w.getCid().getCname())
                    .subrootCid(w.getSubrootCid().getCid())
                    .subrootCname(w.getSubrootCid().getCname())
                    .rootCid(w.getRootCid().getCid())
                    .rootCname(w.getRootCid().getCname())
                    .pid(w.getPid().getPid())
                    .pname(w.getPid().getPname())
                    .email(w.getEmail())
                    .phone(w.getPhone())
                    .registerAt(w.getRegisterAt())
                    .loginAt(w.getLoginAt())
                    .allPushNotice(w.getAllPushNotice())
                    .build();
            res.add(userDto);
        }
        log.info("getAllUserInRootCidCid return : {}", res);
        return res;
    }

    /**
     *  Admin Page에서 사용자 갱신
     */
    public UserDto updateUserByAdmin(AdminEditUserRequest adminEditUserRequest) {
        log.info("updateUserByAdmin call : {}", adminEditUserRequest);

        WorksmileUsers worksmileUsers = worksmileUsersRepo.findByUid(adminEditUserRequest.getUid());
        Companys subrootCompany = companysRepo.findCompanyByCid(adminEditUserRequest.getSubrootCid());
        Companys companys = companysRepo.findCompanyByCid(adminEditUserRequest.getCid());
        Positions positions = positionsRepo.findPositionByPid(adminEditUserRequest.getPid());

        // 기존 worksmileUsers 갱신 후 save
        worksmileUsers = WorksmileUsers.builder()
                .uuid(worksmileUsers.getUuid())
                .uid(worksmileUsers.getUid())
                .pwd(worksmileUsers.getPwd())
                .salt(worksmileUsers.getSalt())
                .uname(worksmileUsers.getUname())
                .profile(worksmileUsers.getProfile())
                .role(adminEditUserRequest.getRole())
                .cid(companys)
                .subrootCid(subrootCompany)
                .rootCid(worksmileUsers.getRootCid())
                .pid(positions)
                .email(worksmileUsers.getEmail())
                .phone(worksmileUsers.getPhone())
                .registerAt(worksmileUsers.getRegisterAt())
                .loginAt(worksmileUsers.getLoginAt())
                .allPushNotice(worksmileUsers.getAllPushNotice())
                .build();
        worksmileUsersRepo.save(worksmileUsers);

        // React의 state에 저장하기 위해 Dto로 반환
        UserDto res = UserDto.builder()
                .uuid(null)
                .uid(worksmileUsers.getUid())
                .pwd(null)
                .salt(null)
                .uname(worksmileUsers.getUname())
                .profile(worksmileUsers.getProfile())
                .role(worksmileUsers.getRole())
                .cid(worksmileUsers.getCid().getCid())
                .cname(worksmileUsers.getCid().getCname())
                .subrootCid(worksmileUsers.getSubrootCid().getCid())
                .subrootCname(worksmileUsers.getSubrootCid().getCname())
                .rootCid(worksmileUsers.getRootCid().getCid())
                .rootCname(worksmileUsers.getRootCid().getCname())
                .pid(worksmileUsers.getPid().getPid())
                .pname(worksmileUsers.getPid().getPname())
                .email(worksmileUsers.getEmail())
                .phone(worksmileUsers.getPhone())
                .registerAt(worksmileUsers.getRegisterAt())
                .loginAt(worksmileUsers.getLoginAt())
                .allPushNotice(worksmileUsers.getAllPushNotice())
                .build();

        log.info("updateUserByAdmin return : {}", res);
        return res;
    }

    /**
     *  Admin Page에서 사용자 삭제
     */
    @Transactional
    public boolean deleteUserByAdmin(String uid) {
        log.info("deleteUserByAdmin call : {}", uid);
        boolean res = false;
        try {
            if(worksmileUsersRepo.deleteByUid(uid) == 1) {
                res = true;
            }
        } catch (Exception e) {
            res = false;
        }
        log.info("deleteUserByAdmin return : {}", res);
        return res;
    }

    /**
     *  비속어 리스트에 비속어 추가
     */
    public ForbiddenWordDto insertForbiddenWord(ForbiddenWordDto forbiddenWordDto) {
        log.info("insertForbiddenWord call : {}", forbiddenWordDto);

        // save
        ForbiddenWords forbiddenWords = forbiddenWordsRepo.save(ForbiddenWords.builder()
                                                                .wid(0)
                                                                .word(forbiddenWordDto.getWord())
                                                                .build());
        ForbiddenWordDto res = ForbiddenWordDto.builder()
                                            .wid(forbiddenWords.getWid())
                                            .word(forbiddenWords.getWord())
                                            .build();
        log.info("insertForbiddenWord return : {}", res);
        return res;
    }

    /**
     *  비속어 삭제
     */
    @Transactional
    public void deleteForbbidenWord(ForbiddenWordDto forbiddenWordDto) {
        log.info("deleteForbbidenWord call : {}", forbiddenWordDto);

        // delete
        forbiddenWordsRepo.delete(ForbiddenWords.builder()
                                                .wid(forbiddenWordDto.getWid())
                                                .word(forbiddenWordDto.getWord())
                                                .build());
        log.info("deleteForbbidenWord return : {}");
        return;
    }


    /**
     *  비속어 리스트 모두 조회
     */
    public List<ForbiddenWordDto> findAllForbiddenWords(ForbiddenWordDto forbiddenWordDto, int mode)  {
        log.info("findAllForbiddenWords call : {}", forbiddenWordDto);

        if(mode == 1) {
            insertForbiddenWord(forbiddenWordDto);
        } else if (mode == 2) {
            deleteForbbidenWord(forbiddenWordDto);
        }
        List<ForbiddenWords> entityList = forbiddenWordsRepo.findAll();
        List<ForbiddenWordDto> res = new ArrayList<>();
        forbiddenWordDto = new ForbiddenWordDto();

        for(ForbiddenWords f : entityList) {
            forbiddenWordDto = ForbiddenWordDto.builder()
                                            .wid(f.getWid())
                                            .word(f.getWord())
                                            .build();
            res.add(forbiddenWordDto);
        }

        log.info("findAllForbiddenWords return : {}", res);
        return res;
    }

}
