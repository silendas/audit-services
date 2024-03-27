package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Optional;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.InspectionSchedule.models.Schedule;

public interface AuditDailyReportRepository extends JpaRepository<AuditDailyReport, Long> {

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.created_at BETWEEN CURRENT_DATE AND CURRENT_TIMESTAMP AND u.schedule_id = :scheduleId AND u.is_delete <> 1;", nativeQuery = true)
    List<AuditDailyReport> findByCurrentDay(@Param("scheduleId") Long id);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.schedule_id = :id AND u.is_delete <> 1;", nativeQuery = true)
    List<AuditDailyReport> findByScheduleId(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.created_at BETWEEN :start_date AND :end_date;", nativeQuery = true)
    public List<AuditDailyReport> findLHAInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.is_delete <> 1", nativeQuery = true)
    List<AuditDailyReport> findAllLHA();

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.id = :id AND u.is_delete <> 1", nativeQuery = true)
    Optional<AuditDailyReport> findOneByLHAId(@Param("id") Long id);
}
