package com.cms.audit.api.InspectionSchedule.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleRequest;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_SPECIAL_SCHEDULE)
public class SpecialScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam("branch_id") Optional<Long> branch_id,
            @RequestParam("name") Optional<String> name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
                Long branchId;
            if(branch_id.isPresent()){
                if(branch_id.get().toString() != ""){
                    branchId = branch_id.get();
                } else {
                    branchId = null;
                }
            } else {
                branchId = null;
            }
            String fullname;
            if(name.isPresent()){
                if(name.get().toString() != ""){
                    fullname = name.get();
                } else {
                    fullname = null;
                }
            } else {
                fullname = null;
            }
            Date startDate;
            if(start_date.isPresent()){
                if(start_date.get().toString() != ""){
                    startDate = start_date.get();
                } else {
                    startDate = null;
                }
            } else {
                startDate = null;
            }
            Date endDate;
            if(end_date.isPresent()){
                if(end_date.get().toString() != ""){
                    endDate = end_date.get();
                } else {
                    endDate = null;
                }
            } else {
                endDate = null;
            }
            Integer pages;
            if(page.isPresent()){
                if(page.get().toString() != ""){
                    pages = page.get();
                } else {
                    pages = 0;
                }
            } else {
                pages = 0;
            }
            Integer sizes;
            if(size.isPresent()){
                if(size.get().toString() != ""){
                    sizes = size.get();
                } else {
                    sizes = 10;
                }
            } else {
                sizes = 10;
            }
        GlobalResponse response = scheduleService.getSpecialSchedule(branchId,fullname,pages,sizes,
                startDate, endDate);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ScheduleRequest scheduleDTO, @Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.insertSpecialSchedule(scheduleDTO, username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditScheduleDTO scheduleDTO, @PathVariable("id") Long id,
            @Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.editSchedule(scheduleDTO, id, ECategory.SPECIAL, username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
