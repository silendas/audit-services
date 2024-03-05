package com.cms.audit.api.LHA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.audit.api.LHA.models.AuditDailyReport;

public interface AuditDailyReportRepository extends JpaRepository<AuditDailyReport, Long>{
    
}
