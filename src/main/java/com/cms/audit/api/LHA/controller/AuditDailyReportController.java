package com.cms.audit.api.LHA.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.LHA.service.AuditDailyReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_LHA)
public class AuditDailyReportController {

    @Autowired
    private AuditDailyReportService auditDailyReportService;

    @GetMapping("/get")
    public ResponseEntity<Object> get() {
        GlobalResponse response = auditDailyReportService.get();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
