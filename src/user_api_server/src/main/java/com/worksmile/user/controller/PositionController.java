package com.worksmile.user.controller;

import com.worksmile.user.dto.ApiResult;
import com.worksmile.user.dto.PositionDto;
import com.worksmile.user.service.CompanysService;
import com.worksmile.user.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/user")
public class PositionController {
    private PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }
    @GetMapping("/positionlist")
    public ApiResult findAllPosition() {
        log.info("findAllPosition call");
        List<PositionDto> positions = positionService.findAllPosition();
        ApiResult res = new ApiResult(200, "직급 추출", positions);
        log.info("findAllPosition return : {}", res);
        return res;
    }
}
