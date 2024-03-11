package com.cms.audit.api.InspectionSchedule.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.InspectionSchedule.dto.EditStatusDTO;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_NEED_APPROVE)
public class NeedApproveController {

    @Autowired
    private ScheduleService service;

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = service.getByStatus("NA", page.orElse(0), size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = service.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Object> rescheduleApprove(@PathVariable("id") Long id, EditStatusDTO dto) {
        GlobalResponse response = service.editStatus(id, EStatus.TODO, dto.getUpdate_by());
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Object> rescheduleRejected(@PathVariable("id") Long id, EditStatusDTO dto) {
        GlobalResponse response = service.editStatus(id, EStatus.REJECTED, dto.getUpdate_by());
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
