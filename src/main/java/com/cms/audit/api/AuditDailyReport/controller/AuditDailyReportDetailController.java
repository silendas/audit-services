package com.cms.audit.api.AuditDailyReport.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDetailDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDetailDTO;
import com.cms.audit.api.AuditDailyReport.service.AuditDailyReportDetailService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_LHA_DETAIL)
public class AuditDailyReportDetailController {

    @Autowired
    private AuditDailyReportDetailService service;

    @GetMapping
    public ResponseEntity<Object> get(
            @RequestParam("lha_id") Optional<Long> lha_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
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
        GlobalResponse response = service.get(page.orElse(0), size.orElse(10), lha_id.orElse(null), startDate, endDate);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        GlobalResponse response = service.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody AuditDailyReportDetailDTO dto) {
        GlobalResponse response = service.save(dto);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditAuditDailyReportDetailDTO dto, @PathVariable("id") Long id) {
        GlobalResponse response = service.edit(dto, id);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @PatchMapping("/send/{id}")
    public ResponseEntity<Object> send(@PathVariable("id") Long id) throws Exception {
        GlobalResponse response = service.sendToLeader(id);
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

    @PatchMapping("/ignore/{id}")
    public ResponseEntity<Object> ignore(@PathVariable("id") Long id) {
        GlobalResponse response = service.ingnoreLhaDetail(id);
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = service.delete(id);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

}
