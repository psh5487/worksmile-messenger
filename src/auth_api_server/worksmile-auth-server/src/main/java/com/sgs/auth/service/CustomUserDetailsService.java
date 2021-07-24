package com.sgs.auth.service;

import com.sgs.auth.model.SecurityUser;
import com.sgs.auth.model.User;
import com.sgs.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) {
        User user = userRepository.findByUid(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + "는 존재하지 않는 사용자입니다."));
        return new SecurityUser(user);
    }
}
