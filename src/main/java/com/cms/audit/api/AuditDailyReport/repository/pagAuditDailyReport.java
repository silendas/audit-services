package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Date;
import java.util.List;
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

    @Query(value = "SELECT * FROM audit_daily_report u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE '% :name %' AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<AuditDailyReport> findLHANameInDateRange(@Param("name") String name, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE '% :name %'", nativeQuery = true)
    Page<AuditDailyReport> findLHAName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :id AND u.created_at BETWEEN :start_date AND :end_date;", nativeQuery = true)
    Page<AuditDailyReport> findLHAByRegionInDateRange(@Param("id") Long area_id,
            @Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :id  AND u.is_delete <> 1;", nativeQuery = true)
    Page<AuditDailyReport> findLHAByRegion(@Param("id") Long area_id, Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN users us ON u.user_id = us.id INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE us.fullname LIKE '% :name %' AND ro.id = :id AND u.created_at BETWEEN :start_date AND :end_date;", nativeQuery = true)
    Page<AuditDailyReport> findLHAByAll(
            @Param("id") Long area_id,
            @Param("name") String name,
            @Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.is_delete <> 1", nativeQuery = true)
    Page<AuditDailyReport> findAllLHA(Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.branch_id = :id AND u.is_delete <> 1", nativeQuery = true)
    Page<AuditDailyReport> findAllLHAByBranch(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report u WHERE u.branch_id = :id AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1", nativeQuery = true)
    Page<AuditDailyReport> findAllLHAByBranchAndDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);

}
