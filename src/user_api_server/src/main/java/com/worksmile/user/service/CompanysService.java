package com.worksmile.user.service;

import com.worksmile.user.domain.entity.CompanyRelations;
import com.worksmile.user.domain.entity.Companys;
import com.worksmile.user.domain.repository.CompanysRepo;
import com.worksmile.user.dto.CompanyDto;
import com.worksmile.user.dto.CompanyRelationDto;
import com.worksmile.user.dto.CompanyTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CompanysService {
    private CompanysRepo companysRepo;

    @Autowired
    public CompanysService(CompanysRepo companysRepo) {
        this.companysRepo = companysRepo;
    }

    /**
     *  option에 따른 Companys Data 가져오기
     */
    public List<CompanyDto> findSubrootCompanys(String option) {
        log.info("findSubrootCompanys call : {}", option);
        List<Companys> companyList = new ArrayList<>();

        if(option.equals("subroot")) {
            companyList.addAll(companysRepo.findSubrootCompanysByIsSubroot("Y"));
        } else if(option.equals("not-subroot")) {
            companyList.addAll(companysRepo.findSubrootCompanysByIsSubroot("N"));
        }

        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Companys companys : companyList) {
            CompanyDto companyDto = CompanyDto.builder()
                    .cid(companys.getCid())
                    .cname(companys.getCname())
                    .isSubroot(companys.getIsSubroot())
                    .build();
            companyDtoList.add(companyDto);
        }
        log.info("findSubrootCompanys return : " + companyDtoList);
        return companyDtoList;
    }

    /**
     *  회사 조직도.
     *  주어진 rootCid 하위의 모든 회사 노드들의 직계 자식 노드를 가진 Collections 반환
     */
    public Map<Integer, CompanyRelationDto> findAllCompanyRelation(int rootCid) {
        log.info("findAllCompanyRelation call : {}", rootCid);
        Map<Integer, CompanyRelationDto> res = new HashMap<>();

        // rootCid의 하위 모든 노드를 찾아서 리스트에 저장
        List<Companys> childNodes = companysRepo.findAllChild(rootCid);

        // childNodes 반복 ; 한 노드의 바로 하위 노드 추출
        for(Companys company : childNodes) {
//            System.out.println(company.getCid() + " // " + company.getCname() + " // " + company.getIsSubroot());
            int parentId = company.getCid();
            res.put(parentId, new CompanyRelationDto());
            res.get(parentId).setCname(company.getCname());


            // 특정 하위 노드의 depth 추출
            int depth = -1;
            for(CompanyRelations cr : company.getChildRelations()) {
                if(cr.getCrId().getParentId().getCid() ==  company.getCid()) {
                    System.out.println("depth : " + cr.getDepth());
                    depth = cr.getDepth();
                    break;
                }
            }

            // 바로 다음 깊이의 노드 리스트 추출 후 DTO 타입 변환
            List<Companys> nextEntities = companysRepo.findNextChild(parentId, depth + 1);
            List<CompanyDto> nextDtos = new ArrayList<>();
            for(Companys c : nextEntities) {
                CompanyDto companyDto = CompanyDto.builder()
                        .cid(c.getCid())
                        .cname(c.getCname())
                        .isSubroot(c.getIsSubroot())
                        .build();
                nextDtos.add(companyDto);
            }

            res.get(parentId).setCompanys(nextDtos);
        }

        log.info("findAllCompanyRelation return : {}", res);
        return res;
    }
}
