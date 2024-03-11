package com.cms.audit.api.AuditDailyReport.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_LHA_DETAIL)
public class AuditDailyReportDetailController {

    @Autowired
    private AuditDailyReportDetailService service;

    @GetMapping
    public ResponseEntity<Object> get(
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = service.get(page.orElse(0), size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        GlobalResponse response = service.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}/lha/all")
    public ResponseEntity<Object> getByLHA(
        @PathVariable("id") Long id,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = service.getByLHAId(id, page.orElse(0),size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}/lha/last")
    public ResponseEntity<Object> getOneByLHA(@PathVariable("id") Long id) {
        GlobalResponse response = service.getOneByLHAId(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody AuditDailyReportDetailDTO dto) {
        GlobalResponse response = service.save(dto);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditAuditDailyReportDetailDTO dto, @PathVariable("id") Long id) {
        GlobalResponse response = service.edit(dto, id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                response.getError());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = service.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                response.getError());
    }

}
