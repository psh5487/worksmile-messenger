package com.sgs.auth.repository;

import com.sgs.auth.model.Companys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Companys, Integer> {
    Optional<Companys> findByCid(Integer cid);
}
