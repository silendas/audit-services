package com.cms.audit.api.Sampling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.RMK.dto.CalculateDto;
import com.cms.audit.api.RMK.service.RmkPlanService;

@RestController
@RequestMapping(value = BasePath.BASE_API + "/calculator")
public class CalculateController {

    @Autowired
    private RmkPlanService service;

    @PostMapping("/slovin")
    public ResponseEntity<Object> calculateRmkPlan(@RequestBody CalculateDto dto){
        return service.calculate(dto.getValue(), dto.getMargin_error());
    }
    
}
