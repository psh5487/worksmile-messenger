package com.sgs.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Companys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int cid;

    @Column(length = 50, nullable = false)
    @ColumnDefault("")
    private String cname;

    @Column(nullable = false, columnDefinition = "char(1)")
    @ColumnDefault("'N'")
    private String isSubroot;

    @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY)
    private List<User> cidUsers = new ArrayList<>();

    @OneToMany(mappedBy = "subrootCid", fetch = FetchType.LAZY)
    private List<User> subrootCidUsers = new ArrayList<>();

    @OneToMany(mappedBy = "rootCid", fetch = FetchType.LAZY)
    private List<User> rootCidUsers = new ArrayList<>();

    @Builder
    public Companys(int cid, String cname, String isSubroot) {
        this.cid = cid;
        this.cname = cname;
        this.isSubroot = isSubroot;
    }
}
