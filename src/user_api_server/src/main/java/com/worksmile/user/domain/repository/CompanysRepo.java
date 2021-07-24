package com.worksmile.user.domain.repository;

import com.worksmile.user.domain.entity.Companys;
import com.worksmile.user.dto.CompanyTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface CompanysRepo extends JpaRepository<Companys, Integer> {
//    @Query("select c from Companys c where c.isSubroot = 'Y'")
    List<Companys> findSubrootCompanysByIsSubroot(String isSubroot);
    Companys findCompanyByCid(int cid);

    @Query("select c from CompanyRelations cr JOIN Companys c on cr.crId.childId.cid = c.cid where cr.crId.parentId.cid = ?1")
    List<Companys> findAllChild(int rootCid);

    @Query("select c from CompanyRelations cr JOIN Companys c on cr.crId.childId.cid = c.cid where cr.crId.parentId.cid = ?1 AND cr.depth = ?2")
    List<Companys> findNextChild(int parentCid, int nextDepth);
}
