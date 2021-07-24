package com.worksmile.user.service;

import com.worksmile.user.domain.entity.Positions;
import com.worksmile.user.domain.repository.PositionsRepo;
import com.worksmile.user.dto.PositionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PositionService {
    private PositionsRepo positionsRepo;

    @Autowired
    public PositionService(PositionsRepo positionsRepo) {
        this.positionsRepo = positionsRepo;
    }

    /**
     *  직급 데이터 추출
     */
    public List<PositionDto> findAllPosition() {
        log.info("findAllPosition call");
        List<PositionDto> res = new ArrayList<>();
        List<Positions> entityList = positionsRepo.findAll();
        PositionDto positionDto = new PositionDto();
        for(Positions p : entityList) {
            positionDto = PositionDto.builder()
                    .pid(p.getPid())
                    .pname(p.getPname())
                    .build();
            res.add(positionDto);
        }
        log.info("findAllPosition return : {}", res);
        return res;
    }
}
