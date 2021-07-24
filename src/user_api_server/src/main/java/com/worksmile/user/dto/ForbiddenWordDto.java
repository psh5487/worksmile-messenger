package com.worksmile.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForbiddenWordDto {
    private int wid;
    private String word;

    @Builder
    public ForbiddenWordDto(int wid, String word) {
        this.wid = wid;
        this.word = word;
    }
}
