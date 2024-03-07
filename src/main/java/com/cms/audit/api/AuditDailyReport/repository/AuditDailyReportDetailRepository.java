package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;

public interface AuditDailyReportDetailRepository extends JpaRepository<AuditDailyReportDetail, Long>{
    @Query(value = "UPDATE audit_daily_report_detail SET is_delete = 1, updated_at = current_timestamp WHERE id = :Id", nativeQuery = true)
    public AuditDailyReportDetail softDelete(@Param("Id") Long userId);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id", nativeQuery = true)
    Optional<AuditDailyReportDetail> findByLHAId(@Param("id") Long id);

}
