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
public class Positions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int pid; // position id. 직급 id

    @Column(nullable = false, length = 50)
    @ColumnDefault("")
    private String pname;

    @OneToMany(mappedBy = "pid", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @Builder
    public Positions(int pid, String pname) {
        this.pid = pid;
        this.pname = pname;
    }
}
