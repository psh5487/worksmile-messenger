package com.worksmile.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEditUserRequest {
    private String uid;
    private int subrootCid;
    private int cid;
    private int pid;
    private String role;
}
