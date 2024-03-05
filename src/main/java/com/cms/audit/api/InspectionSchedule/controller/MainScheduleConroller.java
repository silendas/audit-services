package com.cms.audit.api.InspectionSchedule.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleFilterDTO;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_MAIN_SCHEDULE)
public class MainScheduleConroller {
    
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        GlobalResponse response = scheduleService.getMainSchedule();
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterByDateRange(@RequestParam Integer userId, @RequestParam Date startDate, @RequestParam Date endDate) {
        GlobalResponse response = scheduleService.getByRangeDateAndUserId(userId, "REGULAR",startDate, endDate);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<Object> getByUserId(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getByUserId(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/region")
    public ResponseEntity<Object> getByRegionId(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getByRegionId(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }
    
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ScheduleDTO scheduleDTO) {
        GlobalResponse response = scheduleService.insertRegularSchedule(scheduleDTO);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/reschedule")
    public ResponseEntity<Object> reschedule(@RequestBody ScheduleDTO scheduleDTO) {
        GlobalResponse response = scheduleService.reSchedule(scheduleDTO);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody ScheduleDTO scheduleDTO, @PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.editRegularSchedule(scheduleDTO, id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
