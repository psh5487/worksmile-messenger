package com.worksmile.user.dto;

import com.worksmile.user.domain.entity.Companys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDto {
    private int cid;
    private String cname;
    private String isSubroot;

    public Companys toEntity() {
        Companys build = Companys.builder()
                .cid(cid)
                .cname(cname)
                .isSubroot(isSubroot)
                .build();
        return build;
    }

    @Builder
    public CompanyDto(int cid, String cname, String isSubroot) {
        this.cid = cid;
        this.cname = cname;
        this.isSubroot = isSubroot;
    }
}
