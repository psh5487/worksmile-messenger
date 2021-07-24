package com.worksmile.messageServer.model.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@Builder
public class ConnectedUser implements Serializable {
    private int deviceCount;
}
