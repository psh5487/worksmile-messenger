package com.sgs.auth.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    String type;

    String uid; // user id

    String pwd;

    String uname; // user name

    String phone;

    String email;

    // @JsonProperty 사용 가능
    Integer subroot_cid;

    String subroot_cname;
}
