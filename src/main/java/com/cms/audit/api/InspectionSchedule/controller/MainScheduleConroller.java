package com.cms.audit.api.InspectionSchedule.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> filterByDateRange(
            @RequestParam Long userId, 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
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

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditScheduleDTO scheduleDTO, @PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.editSchedule(scheduleDTO, id, ECategory.REGULAR);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
