package com.cms.audit.api.InspectionSchedule.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_SPECIAL_SCHEDULE)
public class SpecialScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        GlobalResponse response = scheduleService.getSpecialSchedule();
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterByDateRange(@RequestParam Integer userId, @RequestParam Date startDate,
            @RequestParam Date endDate) {
        GlobalResponse response = scheduleService.getByRangeDateAndUserId(userId, "SPECIAL", startDate, endDate);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

}
