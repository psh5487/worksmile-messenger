package com.worksmile.user.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Positions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int pid;

    @Column(length = 50, nullable = false)
    @ColumnDefault("''")
    private String pname;

    @OneToMany(mappedBy = "pid", fetch = FetchType.LAZY)
    private List<WorksmileUsers> users = new ArrayList<>();

    @Builder
    public Positions(int pid, String pname) {
        this.pid = pid;
        this.pname = pname;
    }

}
