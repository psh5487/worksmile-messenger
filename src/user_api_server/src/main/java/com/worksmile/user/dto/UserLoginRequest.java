package com.worksmile.user.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String uid;
    private String pwd;
}
