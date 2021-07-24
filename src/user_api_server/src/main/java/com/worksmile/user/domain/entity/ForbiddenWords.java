package com.worksmile.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ForbiddenWords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint", nullable = false)
    private int wid;

    @Column(length = 100, nullable = false)
    @ColumnDefault("''")
    private String word;

    @Builder
    public ForbiddenWords(int wid, String word) {
        this.wid = wid;
        this.word = word;
    }
}
