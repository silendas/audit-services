package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;

@Repository
public interface PagAuditDailyReportDetail extends PagingAndSortingRepository<AuditDailyReportDetail, Long> {
    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.is_delete <> 1", nativeQuery = true)
    Page<AuditDailyReportDetail> findByLHAId(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHADetail(Pageable pageable);
}
