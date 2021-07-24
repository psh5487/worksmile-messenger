package com.worksmile.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSettingDto {
    private String uid;
    private String ruuid;
    private String rname;
    private String favoriteType;
    private String pushNotice;
}
