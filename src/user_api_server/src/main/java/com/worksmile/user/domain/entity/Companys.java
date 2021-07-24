package com.worksmile.user.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Companys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int cid;

    @Column(length = 50, nullable = false)
    @ColumnDefault("''")
    private String cname;

    @Column(nullable = false, columnDefinition = "char(1)")
    @ColumnDefault("'N'")
    private String isSubroot;

    @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
    private List<Rooms> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
    private List<WorksmileUsers> cidUsers = new ArrayList<>();

    @OneToMany(mappedBy = "subrootCid", fetch = FetchType.LAZY)
    private List<WorksmileUsers> subrootCidUsers = new ArrayList<>();

    @OneToMany(mappedBy = "rootCid", fetch = FetchType.LAZY)
    private List<WorksmileUsers> rootCidUsers = new ArrayList<>();

    @OneToMany(mappedBy = "crId.parentId", fetch = FetchType.LAZY)
    private List<CompanyRelations> parentRelations = new ArrayList<>();

    @OneToMany(mappedBy = "crId.childId", fetch = FetchType.LAZY)
    private List<CompanyRelations> childRelations = new ArrayList<>();

    @Builder
    public Companys(int cid, String cname, String isSubroot) {
        this.cid = cid;
        this.cname = cname;
        this.isSubroot = isSubroot;
    }
}
