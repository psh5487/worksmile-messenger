package com.worksmile.user.dto;

import com.worksmile.user.domain.entity.Companys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyTree extends Companys {
    private int cid;
    private String cname;
    private String isSubroot;
    private int depth;
}
