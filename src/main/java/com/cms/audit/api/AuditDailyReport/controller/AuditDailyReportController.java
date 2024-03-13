package com.cms.audit.api.AuditDailyReport.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.service.AuditDailyReportService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_LHA)
public class AuditDailyReportController {

    @Autowired
    private AuditDailyReportService auditDailyReportService;

    @GetMapping
    public ResponseEntity<Object> get(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = auditDailyReportService.get(page.orElse(0), size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = auditDailyReportService.getByDateRange(start_date, end_date, page.orElse(0),
                size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<Object> getBySchedule(
            @PathVariable("id") Long id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = auditDailyReportService.getByScheduleId(id,page.orElse(0),size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PostMapping
    public ResponseEntity<Object> post(@ModelAttribute AuditDailyReportDTO dto) {
        GlobalResponse response = auditDailyReportService.save(dto);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@ModelAttribute EditAuditDailyReportDTO dto, @PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.edit(dto, id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                response.getError());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = auditDailyReportService.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                response.getError());
    }

}
