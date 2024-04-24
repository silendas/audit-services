package com.cms.audit.api.AuditDailyReport.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.AuditDailyReport.dto.RevisionDTO;
import com.cms.audit.api.AuditDailyReport.service.RevisionService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_LHA_REVISION)
public class RevisionController {
    @Autowired
    private RevisionService service;

    @GetMapping
    public ResponseEntity<Object> getByDetailId(
        @RequestParam("lha_detail_id") Optional<Long> id
        ) {
        GlobalResponse response = service.getAll(id.orElse(null));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detail(@PathVariable("id") Long id) {
        GlobalResponse response = service.getOne(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }

    @PostMapping
    public ResponseEntity<Object> postMethodName(@RequestBody RevisionDTO dto) {
        GlobalResponse response = service.insertNewRevision(dto);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), response.getError());
    }
    
}
