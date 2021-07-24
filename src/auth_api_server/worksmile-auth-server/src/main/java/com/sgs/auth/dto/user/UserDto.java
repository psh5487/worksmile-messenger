package com.sgs.auth.dto.user;

import com.sgs.auth.model.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class UserDto {

    private String uid;

    private String uname;

    private String profile;

    private String role;

    private Integer cid;

    private String cname;

    private Integer subroot_cid;

    private String subroot_cname;

    private Integer root_cid;

    private String root_cname;

    private Integer pid;

    private String pname;

    private String email;

    private String phone;

    private LocalDateTime register_at;

    private LocalDateTime login_at;

    private String all_push_notice;
}
