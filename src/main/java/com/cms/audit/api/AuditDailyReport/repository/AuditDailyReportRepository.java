package com.cms.audit.api.AuditDailyReport.repository;

import java.util.Optional;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;

@Repository
public interface AuditDailyReportRepository extends JpaRepository<AuditDailyReport, Long> {

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :regionId ORDER BY u.id DESC", nativeQuery = true)
        List<AuditDailyReport> findByRegionId(@Param("regionId") Long id);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :regionId AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        List<AuditDailyReport> findByRegionIdAndDate(@Param("regionId") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.created_at BETWEEN CURRENT_DATE AND CURRENT_TIMESTAMP AND u.schedule_id = :scheduleId AND u.is_delete <> 1;", nativeQuery = true)
        List<AuditDailyReport> findByCurrentDay(@Param("scheduleId") Long id);

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.schedule_id = :id AND u.is_delete <> 1;", nativeQuery = true)
        List<AuditDailyReport> findByScheduleId(@Param("id") Long id);

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1 ;", nativeQuery = true)
        public List<AuditDailyReport> findLHAInDateRange(@Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :regionId AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1;", nativeQuery = true)
        public List<AuditDailyReport> findLHAInDateRangeAndRegion(@Param("regionId")Long id,@Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1;", nativeQuery = true)
        public List<AuditDailyReport> findLHAByNameInDateRange(@Param("name") String name,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.user_id = :id AND u.is_delete <> 1", nativeQuery = true)
        List<AuditDailyReport> findAllLHAByUserId(@Param("id") Long id);

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.user_id = :id AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1", nativeQuery = true)
        List<AuditDailyReport> findAllLHAByUserIdInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :id AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1;", nativeQuery = true)
        public List<AuditDailyReport> findLHAByRegionInDateRange(@Param("id") Long area_id,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM audit_daily_report u WHERE u.branch_id = :id AND u.is_delete <> 1;", nativeQuery = true)
        public List<AuditDailyReport> findLHAByBranch(@Param("id") Long branch_id);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :id  AND u.is_delete <> 1;", nativeQuery = true)
        public List<AuditDailyReport> findLHAByRegion(@Param("id") Long area_id);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :id AND u.user_id = :user_id AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1;", nativeQuery = true)
        public List<AuditDailyReport> findLHAByAll(@Param("id") Long area_id,
                        @Param("user_id") Long userId,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.is_delete <> 1", nativeQuery = true)
        List<AuditDailyReport> findAllLHA();

        @Query(value = "SELECT * FROM audit_daily_report u WHERE u.id = :id AND u.is_delete <> 1", nativeQuery = true)
        Optional<AuditDailyReport> findOneByLHAId(@Param("id") Long id);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN users us ON u.user_id = us.id INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :id AND us.fullname LIKE %:name% AND u.created_at BETWEEN :start_date AND :end_date AND u.is_delete <> 1;", nativeQuery = true)
        List<AuditDailyReport> findLHAByAllFilter(@Param("id") Long area_id,
                        @Param("name") String name,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        // @Query(value = "SELECT * FROM audit_daily_report u INNER JOIN users us ON
        // u.user_id = us.id WHERE us.fullname LIKE %:name% AND u.created_at BETWEEN
        // :start_date AND :end_date AND u.is_delete <> 1", nativeQuery = true)
        // List<AuditDailyReport> findLHAByNameInDateRange(String name, Date start_date,
        // Date end_date);

        @Query(value = "SELECT u.* FROM audit_daily_report u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE %:name% AND u.is_delete <> 1", nativeQuery = true)
        List<AuditDailyReport> findLHAByName(@Param("name") String name);
}
