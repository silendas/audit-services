package com.cms.audit.api.RMK.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.RMK.dto.CalculateDto;
import com.cms.audit.api.RMK.dto.RmkPlanDto;
import com.cms.audit.api.RMK.service.RmkPlanService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_RMK_PLAN)
public class RmkPlanController {

    @Autowired
    private RmkPlanService service;

    @GetMapping
    public ResponseEntity<Object> getRmkPlan(
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ){
        return service.getRmkPlan(pageable.orElse(false), page.orElse(0), size.orElse(10));
    }  

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRmkPlanById(@PathVariable("id") Long id){
        return service.getRmkPlanDetail(id);
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<Object> calculateRmkPlan(@RequestBody CalculateDto dto){
        return service.calculate(dto.getRmk(), null);
    }

    @PostMapping
    public ResponseEntity<Object> createRmkPlan(@RequestBody RmkPlanDto rmkPlan){
        return service.createRmkPlan(rmkPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRmkPlan(@RequestBody RmkPlanDto rmkPlan, @PathVariable("id") Long id){
        return service.updateRmkPlan(rmkPlan, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRmkPlan(@PathVariable("id") Long id){
        return service.deleteRmkPlan(id);
    }
    
}
