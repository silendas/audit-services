package com.cms.audit.api.AuditDailyReport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.RevisionDTO;
import com.cms.audit.api.AuditDailyReport.service.RevisionService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = BasePath.BASE_PATH_LHA_REVISION)
public class RevisionController {
    @Autowired
    private RevisionService service;

    @PostMapping
    public ResponseEntity<Object> postMethodName(@RequestBody RevisionDTO dto) {
        GlobalResponse response = service.insertNewRevision(dto);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }
    
}
