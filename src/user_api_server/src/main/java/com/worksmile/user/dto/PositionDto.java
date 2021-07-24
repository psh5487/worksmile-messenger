package com.worksmile.user.dto;

import com.worksmile.user.domain.entity.Positions;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionDto {
    private int pid;
    private String pname;

    public Positions toEntity() {
        Positions build = Positions.builder()
                .pid(pid)
                .pname(pname)
                .build();
        return build;
    }

    @Builder
    public PositionDto(int pid, String pname) {
        this.pid = pid;
        this.pname = pname;
    }
}
