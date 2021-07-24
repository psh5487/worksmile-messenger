package com.worksmile.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Rooms {
    @Id
    @Column(nullable = false, columnDefinition = "char(36)")
    private String ruuid;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int memcnt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime registerAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime updateAt;

    @Column(length = 30, nullable = false)
    @ColumnDefault("''")
    private String roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false, referencedColumnName = "uid")
    private WorksmileUsers uid;

    @Column(length = 50, nullable = false)
    @ColumnDefault("''")
    private String initName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_cid", nullable = false)
    private Companys cid;

    @OneToMany(mappedBy = "ruId.ruuid", fetch = FetchType.LAZY)
    private List<RoomUser> roomUsers = new ArrayList<>();

    @Builder
    public Rooms(String ruuid,
                 int memcnt,
                 LocalDateTime registerAt,
                 LocalDateTime updateAt,
                 String roomType,
                 WorksmileUsers uid,
                 String initName,
                 Companys cid) {
        this.ruuid = ruuid;
        this.memcnt = memcnt;
        this.registerAt = registerAt;
        this.updateAt = updateAt;
        this.roomType = roomType;
        this.uid = uid;
        this.initName = initName;
        this.cid = cid;
    }
}
