package com.sgs.auth.model;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {

    private static final long serialVersionUid = 1L;

    public SecurityUser(com.sgs.auth.model.User user) {
        super(user.getUid(), user.getPwd(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
    }
}
