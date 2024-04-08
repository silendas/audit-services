package com.cms.audit.api.AuditDailyReport.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.service.AuditDailyReportService;
import com.cms.audit.api.Common.constant.BasePath;
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
                GlobalResponse response = auditDailyReportService.get(page.orElse(0), size.orElse(10),
                                start_date.orElse(null), end_date.orElse(null), schedule_id.orElse(null),
                                branch_id.orElse(null), name.orElse(null));
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
                @RequestParam(required = false) Optional<Long> branch_id,
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                @RequestParam("page") Optional<Integer> page,
                @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = auditDailyReportService.getLhaReport(name.orElse(null), branch_id.orElse(null), start_date.orElse(null), end_date.orElse(null), page.orElse(null), size.orElse(null));
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @PostMapping
        public ResponseEntity<Object> post(@RequestBody AuditDailyReportDTO dto) {
                GlobalResponse response = auditDailyReportService.save(dto);
                return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                                response.getError());
        }

        @PutMapping("/{id}")
        public ResponseEntity<Object> put(@RequestBody EditAuditDailyReportDTO dto, @PathVariable("id") Long id) {
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
