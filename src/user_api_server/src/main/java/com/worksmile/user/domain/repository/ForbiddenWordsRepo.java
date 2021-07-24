package com.worksmile.user.domain.repository;

import com.worksmile.user.domain.entity.ForbiddenWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForbiddenWordsRepo extends JpaRepository<ForbiddenWords, Integer> {

}
