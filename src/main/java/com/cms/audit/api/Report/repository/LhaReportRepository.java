package com.cms.audit.api.Report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;

@Repository
public interface LhaReportRepository
        extends JpaRepository<AuditDailyReport, Long>, JpaSpecificationExecutor<AuditDailyReport> {

}
