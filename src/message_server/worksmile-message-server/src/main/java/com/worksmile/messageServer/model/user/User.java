package com.worksmile.messageServer.model.user;

import com.worksmile.messageServer.model.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "worksmile_users", indexes = @Index(name="uid_idx", columnList = "uid"))
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class User {
    @Id
    @Column(nullable = false, length = 36, columnDefinition = "char(36)")
    private String uuid; // 사용자 식별 코드

    @Setter
    @Column(nullable = false, unique = true, length = 45)
    private String uid; // 사용자 아이디

    @Setter
    @Column(nullable = false, columnDefinition = "char(60)")
    private String pwd;

    @Setter
    @Column(length = 45)
    private String salt;

    @Column(nullable = false, length = 20)
    private String uname; // 사용자 본명

    @Setter
    @Column(columnDefinition = "JSON")
    private String profile;

    @Setter
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Setter
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false, unique = true, columnDefinition = "char(11)")
    @ColumnDefault("'01000000000'")
    private String phone;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime registerAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime loginAt;

    @Setter
    @Column(nullable = false, columnDefinition = "char(3)")
    @ColumnDefault("'on'")
    private String allPushNotice; // 전체 알릶 on/off

    @Setter
    @ColumnDefault("0")
    private Integer cid; // 소속 회사의 리프 Department id

    @Setter
    @ColumnDefault("0")
    private Integer subrootCid; // 소속 회사(ex. 스토브, 메가포트, 홀딩스) 아이디

    @Setter
    @ColumnDefault("0")
    private Integer rootCid; // 최상위 회사(ex. 스마일게이트) 아이디

    @Setter
    @ColumnDefault("0")
    private Integer pid; // position id. 직급 id

    @OneToMany(mappedBy = "uuid", fetch = FetchType.LAZY)
    private List<RoomUser> roomUsers;
}
