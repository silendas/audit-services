package com.cms.audit.api.AuditDailyReport.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.service.AuditDailyReportService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_LHA)
public class AuditDailyReportController {

        @Autowired
        private AuditDailyReportService auditDailyReportService;

        @GetMapping
        public ResponseEntity<Object> get(
                        @RequestParam(required = false) Optional<String> name,
                        @RequestParam(required = false) Optional<Long> branch_id,
                        @RequestParam(required = false) Optional<Long> schedule_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                Long branchId;
                if (branch_id.isPresent()) {
                        if (branch_id.get().toString() != "") {
                                branchId = branch_id.get();
                        } else {
                                branchId = null;
                        }
                } else {
                        branchId = null;
                }
                Long scheduleId;
                if (schedule_id.isPresent()) {
                        if (schedule_id.get().toString() != "") {
                                scheduleId = schedule_id.get();
                        } else {
                                scheduleId = null;
                        }
                } else {
                        scheduleId = null;
                }
                String fullname;
                if (name.isPresent()) {
                        if (name.get().toString() != "") {
                                fullname = name.get();
                        } else {
                                fullname = null;
                        }
                } else {
                        fullname = null;
                }
                Date startDate;
                if (start_date.isPresent()) {
                        if (start_date.get().toString() != "") {
                                startDate = start_date.get();
                        } else {
                                startDate = null;
                        }
                } else {
                        startDate = null;
                }
                if (startDate != null) {
                        startDate = convertDateToRoman.setTimeToZero(startDate);
                }
                Date endDate;
                if (end_date.isPresent()) {
                        if (end_date.get().toString() != "") {
                                endDate = end_date.get();
                        } else {
                                endDate = null;
                        }
                } else {
                        endDate = null;
                }
                if (endDate != null) {
                        endDate = convertDateToRoman.setTimeToLastSecond(endDate);
                }
                GlobalResponse response = auditDailyReportService.get(page.orElse(0), size.orElse(10),
                                startDate, endDate, scheduleId,
                                branchId, fullname);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> get(@PathVariable("id") Long id) {
                GlobalResponse response = auditDailyReportService.getById(id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/report")
        public ResponseEntity<Object> getReport(
                        @RequestParam(required = false) Optional<String> name,
                        @RequestParam(required = false) Optional<Long> area_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = auditDailyReportService.getLhaReport(name.orElse(null), area_id.orElse(null),
                                start_date.orElse(null), end_date.orElse(null), page.orElse(0), size.orElse(10));
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @PostMapping
        public ResponseEntity<Object> post(@RequestBody AuditDailyReportDTO dto) {
                GlobalResponse response = auditDailyReportService.save(dto);
                if (response.getStatus().value() == 400) {
                        return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
                } else {
                        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                                        response.getError());
                }
        }

        @PatchMapping("/{id}")
        public ResponseEntity<Object> put(@RequestBody EditAuditDailyReportDTO dto, @PathVariable("id") Long id) {
                GlobalResponse response = auditDailyReportService.edit(dto, id);
                if (response.getStatus().value() == 400) {
                        return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
                } else {
                        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                                        response.getError());
                }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
                GlobalResponse response = auditDailyReportService.delete(id);
                if (response.getStatus().value() == 400) {
                        return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
                } else {
                        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                                        response.getError());
                }
        }

}
