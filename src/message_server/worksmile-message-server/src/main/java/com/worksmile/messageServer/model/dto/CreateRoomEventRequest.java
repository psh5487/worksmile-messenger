package com.worksmile.messageServer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CreateRoomEventRequest {

    private String ruuid;

    private String rname;

    private List<String> users;
}
