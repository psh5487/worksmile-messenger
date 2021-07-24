package com.worksmile.user.dto;

import com.worksmile.user.domain.entity.Companys;
import com.worksmile.user.domain.entity.Positions;
import com.worksmile.user.domain.entity.WorksmileUsers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDto {
    private String uuid;
    private String uid;
    private String pwd;
    private String salt;
    private String uname;
    private String profile;
    private String role;
    private int cid;
    private String cname;
    private int subrootCid;
    private String subrootCname;
    private int rootCid;
    private String rootCname;
    private int pid;
    private String pname;
    private String email;
    private String phone;
    private LocalDateTime registerAt;
    private LocalDateTime loginAt;
    private String allPushNotice;

    @Builder
    public UserDto(String uuid,
                   String uid,
                   String pwd,
                   String salt,
                   String uname,
                   String profile,
                   String role,
                   int cid,
                   String cname,
                   int subrootCid,
                   String subrootCname,
                   int rootCid,
                   String rootCname,
                   int pid,
                   String pname,
                   String email,
                   String phone,
                   LocalDateTime registerAt,
                   LocalDateTime loginAt,
                   String allPushNotice) {
        this.uuid = uuid;
        this.uid = uid;
        this.pwd = pwd;
        this.salt = salt;
        this.uname = uname;
        this.profile = profile;
        this.role = role;
        this.cid = cid;
        this.cname = cname;
        this.subrootCid = subrootCid;
        this.subrootCname = subrootCname;
        this.rootCid = rootCid;
        this.rootCname = rootCname;
        this.pid = pid;
        this.pname = pname;
        this.email = email;
        this.phone = phone;
        this.registerAt = registerAt;
        this.loginAt = loginAt;
        this.allPushNotice = allPushNotice;
    }
}
