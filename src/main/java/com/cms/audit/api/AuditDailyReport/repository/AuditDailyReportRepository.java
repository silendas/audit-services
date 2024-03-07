package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;

public interface AuditDailyReportRepository extends JpaRepository<AuditDailyReport, Long> {

    @Query(value = "SELECT * FROM audit_dailt_report u WHERE u.created_at = :current_day",nativeQuery = true)
    Optional<AuditDailyReport> findByCurrentDay(@Param("current_day") Date thisDay);

    @Query(value = "UPDATE audit_dailt_report SET is_delete = 1, updated_at = current_timestamp WHERE id = :Id", nativeQuery = true)
    public AuditDailyReport softDelete(@Param("Id") Long userId);
}
