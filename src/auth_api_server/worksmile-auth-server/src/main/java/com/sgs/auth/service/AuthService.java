package com.sgs.auth.service;

import com.sgs.auth.dto.user.JoinRequest;
import com.sgs.auth.model.User;
import com.sgs.auth.model.UserRole;
import javassist.NotFoundException;

import java.util.Optional;

public interface AuthService {

    String VERIFICATION_LINK = "http://localhost:8080/api/user/verify/";

    String CHANGE_PASSWORD_LINK = "http://localhost:8080/api/user/help/pwd/";

    User join(JoinRequest joinRequest);

    void leave(String userId);

    Boolean isUniqueId(String userId);

    User sendRegisterFinishEmail(String type, String userId);

    User login(String email, String password);

    User findUserById(String userId);

    User findUserByEmail(String email);

    User sendVerificationEmail(String userId);

    String verifyEmail(String verificationKey) throws NotFoundException;

    User modifyUserRole(User user, UserRole userRole);

    void changePassword(User user, String newPwd);
}
