package com.worksmile.messageServer.model.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomUserPk implements Serializable {

    private String ruuid;

    private String uuid;
}
