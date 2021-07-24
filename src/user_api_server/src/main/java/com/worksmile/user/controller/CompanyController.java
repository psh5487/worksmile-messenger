package com.worksmile.user.controller;

import com.worksmile.user.domain.entity.Companys;
import com.worksmile.user.dto.ApiResult;
import com.worksmile.user.dto.CompanyDto;
import com.worksmile.user.dto.CompanyRelationDto;
import com.worksmile.user.service.CompanysService;
import com.worksmile.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/user")
public class CompanyController {
    private CompanysService companysService;

    @Autowired
    public CompanyController(CompanysService companysService) {
        this.companysService = companysService;
    }

    @GetMapping("/companylist/{option}")
    public ApiResult findSubrootCompanys(@PathVariable String option) {
        log.info("findSubrootCompanys call : {}", option);
        List<CompanyDto> companyDtoList = companysService.findSubrootCompanys(option);
        ApiResult res = new ApiResult(200, "회사 리스트 추출", companyDtoList);
        log.info("findSubrootCompanys return : " + res);
        return res;
    }

    @GetMapping("/organization/root-company/{rootCid}")
    public ApiResult findAllCompanyRelation(@PathVariable int rootCid) {
        log.info("findAllCompanyRelation call : {}", rootCid);
        Map<Integer, CompanyRelationDto> map = companysService.findAllCompanyRelation(rootCid);

        ApiResult res = new ApiResult(200, "test", map);
        log.info("findAllCompanyRelation return : {}", res);
        return res;
    }
}
