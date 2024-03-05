package com.cms.audit.api.LHA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.LHA.models.AuditDailyReport;
import com.cms.audit.api.LHA.repository.AuditDailyReportRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditDailyReportService {
 
    @Autowired
    private AuditDailyReportRepository auditDailyReportRepository;

    public GlobalResponse get(){
        try {
            List<AuditDailyReport> response = auditDailyReportRepository.findAll();
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .data(response)
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
