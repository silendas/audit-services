package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Optional;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;

@Repository
public interface AuditDailyReportDetailRepository extends JpaRepository<AuditDailyReportDetail, Long> {

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<AuditDailyReportDetail> findByLHAId(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.status_parsing <> 1 AND u.status_flow = 1 AND u.is_delete <> 1", nativeQuery = true)
    List<AuditDailyReportDetail> findByLHAIdForLeader(@Param("id") Long id);

    
    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.status_flow = 1 AND u.is_delete <> 1", nativeQuery = true)
    List<AuditDailyReportDetail> findByLHAIdLeader(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.audit_daily_report_id = :id AND u.is_delete <> 1 ORDER BY u.id DESC LIMIT 1", nativeQuery = true)
    Optional<AuditDailyReportDetail> findOneByLHAId(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.is_delete <> 1  ORDER BY u.id ASC ;", nativeQuery = true)
    List<AuditDailyReportDetail> findAllLHADetail();

    @Query(value = "SELECT u.* FROM audit_daily_report_detail u INNER JOIN audit_daily_report adr ON u.audit_daily_report_id=adr.id INNER JOIN branch_office bo ON adr.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.is_delete <> 1 ORDER BY u.id ASC;", nativeQuery = true)
    List<AuditDailyReportDetail> findAllLHAByRegion(@Param("id") Long id);

    @Query(value = "SELECT u.* FROM audit_daily_report_detail u INNER JOIN audit_daily_report adr ON u.audit_daily_report_id=adr.id INNER JOIN branch_office bo ON adr.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND (u.created_at BETWEEN :start_date AND :end_date) AND u.is_delete <> 1 ORDER BY u.id ASC;", nativeQuery = true)
    List<AuditDailyReportDetail> findAllLHAByRegionAndDate(@Param("id") Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date);

    @Query(value = "SELECT * FROM audit_daily_report_detail u WHERE u.id = :id AND u.is_delete <> 1", nativeQuery = true)
    Optional<AuditDailyReportDetail> findOneByLHADetailId(@Param("id") Long id);

}
