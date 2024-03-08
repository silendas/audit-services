package com.cms.audit.api.AuditDailyReport.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.service.AuditDailyReportService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = BasePath.BASE_PATH_LHA)
public class AuditDailyReportController {

    @Autowired
    private AuditDailyReportService auditDailyReportService;

    @GetMapping
    public ResponseEntity<Object> get() {
        GlobalResponse response = auditDailyReportService.get();
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<Object> getBySchedule(@PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.getByScheduleId(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody AuditDailyReportDTO dto) {
        GlobalResponse response = auditDailyReportService.save(dto);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), response.getError());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditAuditDailyReportDTO dto, @PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.edit(dto, id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), response.getError());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), response.getError());
    }

}
