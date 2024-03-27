package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;


public interface AuditDailyReportDetailRepository extends JpaRepository<AuditDailyReportDetail, Long> {

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<AuditDailyReportDetail> findByLHAId(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.is_delete <> 1 ORDER BY u.id DESC LIMIT 1", nativeQuery = true)
    Optional<AuditDailyReportDetail> findOneByLHAId(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    List<AuditDailyReportDetail> findAllLHADetail();

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.id = :id AND u.is_delete <> 1", nativeQuery = true)
    Optional<AuditDailyReportDetail> findOneByLHADetailId(@Param("id") Long id);

}
