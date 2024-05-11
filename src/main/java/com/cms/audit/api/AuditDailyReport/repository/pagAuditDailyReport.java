package com.cms.audit.api.AuditDailyReport.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;

@Repository
public interface pagAuditDailyReport extends PagingAndSortingRepository<AuditDailyReport, Long>, JpaSpecificationExecutor<AuditDailyReport> {

}
