package com.worksmile.user.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name="uid_idx", columnList = "uid"))
public class WorksmileUsers implements Serializable{
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(columnDefinition = "char(36)", length = 36, nullable = false)
    private String uuid;

    @Column(nullable = false, length = 45, unique = true)
    @ColumnDefault("''")
    private String uid;

    @Column(nullable = false, columnDefinition = "char(60)")
    @ColumnDefault("''")
    private String pwd;

    @Column(length = 45)
    @ColumnDefault("''")
    private String salt;

    @Column(length = 20, nullable = false)
    @ColumnDefault("''")
    private String uname;

    @Column(length = 255, nullable = true)
    @ColumnDefault("''")
    private String profile;

    @Column(length = 50, nullable = false)
    @ColumnDefault("''")
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid", nullable = false)
    @ColumnDefault("0")
    private Companys cid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subroot_cid", nullable = false)
    @ColumnDefault("0")
    private Companys subrootCid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_cid", nullable = false)
    @ColumnDefault("0")
    private Companys rootCid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", nullable = false)
    @ColumnDefault("0")
    private Positions pid;

    @Column(length = 100, nullable = false)
    @ColumnDefault("''")
    private String email;

    @Column(nullable = false, columnDefinition = "char(11)")
    @ColumnDefault("'01000000000'")
    private String phone;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime registerAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime loginAt;

    @Column(nullable = false, columnDefinition = "char(3)")
    @ColumnDefault("'on'")
    private String allPushNotice;

    @OneToMany(mappedBy = "uid", fetch = FetchType.LAZY)
    private List<Rooms> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "ruId.uuid", fetch = FetchType.LAZY)
    private List<RoomUser> roomUsers = new ArrayList<>();

    @Builder
    public WorksmileUsers(String uuid,
                          String uid,
                          String pwd,
                          String salt,
                          String uname,
                          String profile,
                          String role,
                          Companys cid,
                          Companys subrootCid,
                          Companys rootCid,
                          Positions pid,
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
        this.subrootCid = subrootCid;
        this.rootCid = rootCid;
        this.pid = pid;
        this.email = email;
        this.phone = phone;
        this.registerAt = registerAt;
        this.loginAt = loginAt;
        this.allPushNotice = allPushNotice;
    }

    public void toggleAllPushNotice() {
        allPushNotice = allPushNotice.equals("on") ? "off" : "on";
    }
}
