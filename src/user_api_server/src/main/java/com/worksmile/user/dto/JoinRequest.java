package com.worksmile.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    private String uid;
    private String pwd;
    private String uname;
    private String phone;
    private String email;
    private int cid;
}
