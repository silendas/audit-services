package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;

@Repository
public interface PagAuditDailyReportDetail extends PagingAndSortingRepository<AuditDailyReportDetail, Long> {
    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.is_delete <> 1 ORDER BY u.id ASC ", nativeQuery = true)
    Page<AuditDailyReportDetail> findByLHAId(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report_detail u INNER JOIN audit_daily_report pr ON u.audit_daily_report_id=pr.id WHERE pr.user_id = :id AND u.is_delete <> 1 ORDER BY u.id ASC ", nativeQuery = true)
    Page<AuditDailyReportDetail> findByUserId(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report_detail u INNER JOIN audit_daily_report pr ON u.audit_daily_report_id=pr.id WHERE pr.user_id = :id AND (u.created_at BETWEEN :start_date AND :end_date) AND u.is_delete <> 1 ORDER BY u.id ASC ", nativeQuery = true)
    Page<AuditDailyReportDetail> findByUserIdAndDate(@Param("id") Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHADetail(Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.status_flow = 1 AND u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHADetailForLeader(Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report_detail u INNER JOIN audit_daily_report adr ON u.audit_daily_report_id=adr.id INNER JOIN branch_office bo ON adr.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.is_delete <> 1 ORDER BY u.id ASC;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHAByRegion(@Param("id") Long id,Pageable pageable);

    @Query(value = "SELECT u.* FROM audit_daily_report_detail u INNER JOIN audit_daily_report adr ON u.audit_daily_report_id=adr.id INNER JOIN branch_office bo ON adr.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND (u.created_at BETWEEN :start_date AND :end_date) AND u.is_delete <> 1 ORDER BY u.id ASC;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHAByRegionAndDate(@Param("id") Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date,Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE (u.created_at BETWEEN :start_date AND :end_date) AND u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHADetailByDate(@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE (u.created_at BETWEEN :start_date AND :end_date) AND u.status_flow = 1 AND u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHADetailByDateForLeader(@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);

    
    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE (u.created_at BETWEEN :start_date AND :end_date) AND u.audit_daily_report_id = :id AND u.is_delete <> 1 ORDER BY u.id ASC ;", nativeQuery = true)
    Page<AuditDailyReportDetail> findAllLHADetailByDateAndLhaId(@Param("id") Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);
}
