package com.sgs.auth.service;

import com.sgs.auth.dto.user.JoinRequest;
import com.sgs.auth.model.Companys;
import com.sgs.auth.model.Positions;
import com.sgs.auth.model.User;
import com.sgs.auth.model.UserRole;
import com.sgs.auth.repository.CompanyRepository;
import com.sgs.auth.repository.PositionRepository;
import com.sgs.auth.repository.UserRepository;
import com.sgs.auth.util.RedisUtil;
import com.sgs.auth.util.SaltUtil;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PositionRepository positionRepository;
    private final SaltUtil saltUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, PositionRepository positionRepository, SaltUtil saltUtil, RedisUtil redisUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.positionRepository = positionRepository;
        this.saltUtil = saltUtil;
        this.redisUtil = redisUtil;
        this.emailService = emailService;
    }

    /**
     * 회원가입
     * */
    @Override
    @Transactional // 공부 더 해보기 // 롤백 필요성 있을 수도
    public User join(JoinRequest joinRequest) {
        String userId = joinRequest.getUid();
        String registerType = joinRequest.getType();
        String name = joinRequest.getUname();
        String password = joinRequest.getPwd();
        String email = joinRequest.getEmail();
        String phone = joinRequest.getPhone();
        Integer subrootCid = joinRequest.getSubroot_cid();

        // 중복 체크
        userRepository.findByUid(userId).ifPresent((user) -> {
            if(user.getRole().equals(UserRole.ROLE_NOT_PERMITTED_USER) || user.getRole().equals(UserRole.ROLE_NOT_PERMITTED_ADMIN)) {
                // 인증 못 받은 사용자로 남아있을 경우, 삭제하기 (인증 토큰 유효 시간 끝났을 경우 고려함)
                userRepository.deleteByUid(userId);
            } else {
                throw new RuntimeException("이미 가입된 사용자입니다.");
            }
        });

        // registerType(userRegister / adminRegister) 에 따라 초기 Role 다름
        UserRole registerRole = UserRole.ROLE_NOT_PERMITTED_USER;

        if(registerType.equals("adminRegister")) {
            registerRole = UserRole.ROLE_NOT_PERMITTED_ADMIN;
        }

        Companys companys = companyRepository.findByCid(subrootCid)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사입니다."));

        Positions positions = positionRepository.findByPid(0)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 직급입니다."));

        // Salt 생성
        String salt = saltUtil.genSalt();

        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .uid(userId)
                .pwd(saltUtil.encodePassword(salt, password))
                .salt(salt)
                .uname(name)
                .profile("https://s3-ap-northeast-1.amazonaws.com/jobjava/image_admin_210202_172242_defaultProfile.PNG")
                .role(registerRole)
                .cid(companys)
                .subrootCid(companys)
                .rootCid(companys)
                .pid(positions)
                .email(email)
                .phone(phone)
                .registerAt(LocalDateTime.now())
                .allPushNotice("on")
                .build();

        return userRepository.save(user);
    }

    /**
     * 회원 탈퇴
     * */
    @Override
    @Transactional
    public void leave(String userId) {
        checkNotNull(userId, "사용자 ID 는 필수 값입니다.");
        userRepository.deleteByUid(userId);
    }

    /**
     * 아이디 중복 확인
     * */
    @Override
    public Boolean isUniqueId(String userId) {
        checkNotNull(userId, "사용자 ID 는 필수 값입니다.");

        if(userRepository.findByUid(userId).isPresent()) {
            return false;
        }
        return true;
    }

    /**
     * 로그인
     * */
    @Override
    public User login(String userId, String password) {

        checkNotNull(userId, "사용자 ID 는 필수 값입니다.");
        checkNotNull(password, "Password 는 필수 값입니다.");

        // 해당 사용자 가입 여부 확인
        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 사용자입니다."));

        // 가입 승인 미완료
        if(user.getRole() == UserRole.ROLE_NOT_PERMITTED_USER) {
            throw new RuntimeException("가입 승인이 완료되지 않았습니다.");
        }

        // 비밀번호 일치 여부 확인
        String salt = user.getSalt();
        String encodedPassword = saltUtil.encodePassword(salt, password);

        if (!user.getPwd().equals(encodedPassword))
            throw new RuntimeException("잘못된 비밀번호입니다.");

        return user;
    }

    /**
     * id 로 사용자 찾기
     * */
    @Override
    @Transactional(readOnly = true)
    public User findUserById(String userId) {
        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        return user;
    }

    /**
     * email 로 사용자 찾기
     * */
    @Override
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        // 해당 사용자 가입 여부 확인
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("아이디 찾기 실패. 가입되지 않은 사용자입니다."));
        return user;
    }

    /**
     * 가입 승인 완료 Email 보내기
     * */
    @Override
    public User sendRegisterFinishEmail(String type, String userId) {

        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 사용자입니다."));

        if(type.equals("userRegister")) {
            user.setRole(UserRole.ROLE_USER);
        } else if(type.equals("adminRegister")) {
            user.setRole(UserRole.ROLE_COMPANY_ADMIN);
        }

        userRepository.save(user);
        emailService.sendMail(user.getEmail(), "[WorkSmile] Welcome WorkSmile", user.getUname() + "님 가입이 승인되셨습니다.");
        return user;
    }

    /**
     * 비밀번호 변경 Email 보내기
     * */
    @Override
    public User sendVerificationEmail(String userId) {

        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 사용자입니다."));

        String verificationKey = UUID.randomUUID().toString();
        redisUtil.setDataExpire(verificationKey, user.getUid(), RedisUtil.EMAIL_VERIFICATION_KEY_DURATION);

        emailService.sendMail(user.getEmail(), "[WorkSmile] 비밀번호 변경 메일입니다. 링크를 눌러 초기화된 비밀번호를 확인하세요.", CHANGE_PASSWORD_LINK + verificationKey);
        return user;
    }

    /**
     * 비밀번호 변경 Email Key 확인 후, Reset Pwd 발급
     * */
    @Override
    public String verifyEmail(String verificationKey) throws NotFoundException {

        String userId = redisUtil.getData(verificationKey);
        redisUtil.deleteData(verificationKey);

        if(userId == null) {
            throw new NotFoundException("인증 시간이 만료되었습니다.");
        }

        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new NotFoundException("가입되지 않은 사용자입니다."));

        String resetPwd = UUID.randomUUID().toString().substring(0, 10);
        changePassword(user, resetPwd);
        return resetPwd;
    }

    // 비밀번호 변경하기
    @Override
    public void changePassword(User user, String newPwd) {
        String salt = saltUtil.genSalt();
        user.setSalt(salt);
        user.setPwd(saltUtil.encodePassword(salt, newPwd));
        userRepository.save(user);
    }

    // 사용자 Role 변경하기
    @Override
    public User modifyUserRole(User user, UserRole userRole) {
        user.setRole(userRole);
        return userRepository.save(user);
    }
}
