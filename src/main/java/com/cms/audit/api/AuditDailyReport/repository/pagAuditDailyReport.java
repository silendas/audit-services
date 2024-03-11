package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;

@Repository
public interface pagAuditDailyReport extends PagingAndSortingRepository<AuditDailyReport, Long> {

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.created_at BETWEEN CURRENT_DATE AND CURRENT_TIMESTAMP AND u.schedule_id = :scheduleId AND u.is_delete <> 1;", nativeQuery = true)
    Page<AuditDailyReport> findByCurrentDay(@Param("scheduleId") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.schedule_id = :id AND u.is_delete <> 1", nativeQuery = true)
    Page<AuditDailyReport> findByScheduleId(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<AuditDailyReport> findLHAInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.is_delete <> 1", nativeQuery = true)
    Page<AuditDailyReport> findAllLHA(Pageable pageable);

}
