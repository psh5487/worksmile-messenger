package com.sgs.auth.service;

import com.sgs.auth.dto.user.UserDto;
import com.sgs.auth.model.User;
import com.sgs.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> allUserInfo() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(user -> makeUserDto(user))
                .collect(Collectors.toList());
        return userDtos;
    }

    public UserDto findUserById(String userId) {
        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다"));
        return makeUserDto(user);
    }

    public UserDto makeUserDto(User user) {
        return UserDto.builder()
                .uid(user.getUid())
                .uname(user.getUname())
                .profile(user.getProfile())
                .role(user.getRole().name())
                .cid(user.getCid().getCid())
                .cname(user.getCid().getCname())
                .subroot_cid(user.getSubrootCid().getCid())
                .subroot_cname(user.getSubrootCid().getCname())
                .root_cid(user.getRootCid().getCid())
                .root_cname(user.getRootCid().getCname())
                .pid(user.getPid().getPid())
                .pname(user.getPid().getPname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .register_at(user.getRegisterAt())
                .login_at(user.getLoginAt())
                .all_push_notice(user.getAllPushNotice())
                .build();
    }
}

