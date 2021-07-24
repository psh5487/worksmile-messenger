package com.worksmile.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
public class RoomUser {
    @EmbeddedId
    private RoomUserId ruId;

    @Column(length = 50, nullable = false)
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

    @Builder
    public RoomUser(RoomUserId ruId,
                    String rname,
                    String favoriteType,
                    String pushNotice,
                    int lastReadIdx) {
        this.ruId = ruId;
        this.rname = rname;
        this.favoriteType = favoriteType;
        this.pushNotice = pushNotice;
        this.lastReadIdx = lastReadIdx;
    }
}
