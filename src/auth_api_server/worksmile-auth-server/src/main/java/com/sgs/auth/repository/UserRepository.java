package com.sgs.auth.repository;

import com.sgs.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUid(String uid);
    Optional<User> findUserByEmail(String email);
    void deleteByUid(String uid);
}
