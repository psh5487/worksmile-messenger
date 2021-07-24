package com.worksmile.messageServer.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@IdClass(RoomUserPk.class)
@Getter
@NoArgsConstructor
public class RoomUser {
    @Id
    @Column(name = "ruuid")
    private String ruuid;

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(nullable = false, length = 50)
    @ColumnDefault("''")
    private String rname;

    @Column(nullable = false, columnDefinition = "char(3)")
    @ColumnDefault("'off'")
    private String favoriteType;

    @Column(nullable = false, columnDefinition = "char(3)")
    @ColumnDefault("'on'")
    private String pushNotice;

    @Column(nullable = false, columnDefinition = "bigint unsigned")
    @ColumnDefault("0")
    private int lastReadIdx;
}
