package com.sgs.auth.repository;

import com.sgs.auth.model.Positions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Positions, Integer> {
    Optional<Positions> findByPid(Integer pid);
}
