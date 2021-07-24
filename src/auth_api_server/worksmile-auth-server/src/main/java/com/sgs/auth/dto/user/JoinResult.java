package com.sgs.auth.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinResult {

    String uid; // user id

    String uname; // user name

    String phone;

    String email;

    Integer subroot_cid;

    String subroot_cname;
}
