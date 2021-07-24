package com.worksmile.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
public class CompanyRelations {

    @EmbeddedId
    private CompanyRelationsId crId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int depth;

    @Builder
    public CompanyRelations(CompanyRelationsId crId, int depth) {
        this.crId = crId;
        this.depth = depth;
    }
}
