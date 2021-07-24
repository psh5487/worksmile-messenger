package com.worksmile.user.domain.repository;

import com.worksmile.user.domain.entity.Companys;
import com.worksmile.user.domain.entity.WorksmileUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorksmileUsersRepo extends JpaRepository<WorksmileUsers, String> {

    // UID로 유저 찾기
//    @Query("select w from WorksmileUsers w where w.uid = ?1")
    WorksmileUsers findByUid(String uid);
    List<WorksmileUsers> findAllByRoleOrRoleOrRole(String companyRole,
                                                    String rootCompanyRole,
                                                    String notPermittedAdminRole);
    List<WorksmileUsers> findAllBySubrootCid(Companys subrootCid);
    List<WorksmileUsers> findAllByRootCid(Companys cid);
    Integer deleteByUid(String uid);

}
