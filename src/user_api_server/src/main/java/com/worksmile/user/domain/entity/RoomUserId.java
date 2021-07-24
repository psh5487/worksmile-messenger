package com.worksmile.user.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class RoomUserId implements Serializable {

    // 단방향 VS 양방향 체크 필요 : 현재는 단방향처리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid", nullable = false)
    private WorksmileUsers uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruuid", nullable = false)
    private Rooms ruuid;
}
