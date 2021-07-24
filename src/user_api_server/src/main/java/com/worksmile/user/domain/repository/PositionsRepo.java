package com.worksmile.user.domain.repository;

import com.worksmile.user.domain.entity.Positions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepo extends JpaRepository<Positions, Integer> {
    Positions findPositionByPid(int pid);
}
