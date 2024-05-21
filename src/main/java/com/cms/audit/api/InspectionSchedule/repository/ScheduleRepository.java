package com.cms.audit.api.InspectionSchedule.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.models.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
        // this is for select

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        public List<Schedule> findAllScheduleByUserId(@Param("userId") Long id, @Param("category") String ucategory);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        public List<Schedule> findAllScheduleByDateRange(@Param("category") String ucategory,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM inspection_schedule u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% AND u.branch_id = :branchId AND u.category = :category AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        public List<Schedule> findAllScheduleByFUllenameAndBranch(@Param("name") String name,
                        @Param("branchId") Long id, @Param("category") String ucategory);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.branch_id = :branchId AND u.category = :category AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        public List<Schedule> findAllScheduleByBranchId(@Param("branchId") Long id,
                        @Param("category") String ucategory);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInDateRangeByUserId(@Param("userId") Long userId,
                        @Param("category") String ucategory, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInDateRangeByUserIdNoCategory(@Param("userId") Long userId,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.branch_id = :branchId AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInDateRangeByBranchIdNoCategory(@Param("branchId") Long branchId,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.status <> 'DONE' AND u.category = 'SPECIAL' AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInDateRangeByUserIdNoCategoryForSpecial(@Param("userId") Long userId,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.id <> :schedule_id AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInDateRangeByUserIdNoCategoryEdit(@Param("userId") Long userId,
                        @Param("schedule_id") Long schdule_id,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.description = :description AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInDateRangeByUserIdNoCategoryForEdit(@Param("userId") Long userId,
                        @Param("description") String description,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.branch_id = :branchId AND u.category = :category AND u.start_date = :start_date AND u.end_date = :end_date AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInAllCheck(@Param("userId") Long userId, @Param("branchId") Long branchId,
                        @Param("category") String ucategory, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.branch_id = :branchId AND u.start_date = :start_date AND u.end_date = :end_date AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInAllCheckNoCheck(@Param("userId") Long userId,
                        @Param("branchId") Long branchId, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.description = :description AND u.branch_id = :branchId AND u.start_date = :start_date AND u.end_date = :end_date AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> findScheduleInAllCheckNoCheckForEdit(@Param("userId") Long userId,
                        @Param("description") String description,
                        @Param("branchId") Long branchId, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.id = :id AND (DATE(CURRENT_TIMESTAMP) BETWEEN DATE(u.start_date) AND DATE(u.end_date) OR DATE(CURRENT_TIMESTAMP) > DATE(u.start_date)) AND u.is_delete = 0 ;", nativeQuery = true)
        public List<Schedule> CheckIfScheduleisNow(@Param("id") Long id);

        @Query(value = "SELECT u.* FROM inspection_schedule u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.category = :category AND u.is_delete <> 1 ORDER BY u.id DESC ;", nativeQuery = true)
        public List<Schedule> findByRegionId(@Param("regionId") Long regionId, @Param("category") String category);

        @Query(value = "SELECT u.* FROM inspection_schedule u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.category = :category AND u.start_date BETWEEN :start_date AND :end_date AND u.end_date BETWEEN :start_date AND :end_date AND u.is_delete <> 1 ORDER BY u.id DESC ;", nativeQuery = true)
        List<Schedule> findScheduleInDateRangeByRegionId(@Param("regionId") Long regionId,
                        @Param("category") String ucategory, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM inspection_schedule u LEFT JOIN audit_working_paper kka ON kka.schedule_id = u.id WHERE u.start_date < :start_date AND u.user_id = :user_id  AND (u.status = 'TODO' OR u.status = 'PROGRESS') AND (kka.file_name IS NULL) AND u.is_delete <> 1 ORDER BY id ASC;", nativeQuery = true)
        List<Schedule> findForScheduleList(@Param("user_id") Long userId, @Param("start_date") Date start_date);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.id = :scheduleId ", nativeQuery = true)
        public Optional<Schedule> findOneScheduleById(@Param("scheduleId") Long scheduleId);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.status = :status AND u.is_delete = 0 ", nativeQuery = true)
        public List<Schedule> findOneScheduleByStatus(@Param("status") String scheduleId);

        // this is for update
        @Modifying(flushAutomatically = true, clearAutomatically = true)
        @Query(value = " UPDATE inspection_schedule SET status = 'PENDING', updated_at = current_timestamp, updated_by = :updatedBy WHERE user_id = :userId AND (start_date BETWEEN :start_date AND :end_date OR end_date BETWEEN :start_date AND :end_date) AND status <> 'DONE' AND status <> 'REQUEST' AND status <> 'REJECTED' AND status <> 'APPROVE' AND status <> 'PENDING' AND category <> 'SPECIAL' AND is_delete <> 1;", nativeQuery = true)
        public void editStatusPendingScheduleByDate(@Param("userId") Long userId, @Param("updatedBy") Long updatedBy,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Modifying(flushAutomatically = true, clearAutomatically = true)
        @Query(value = " UPDATE inspection_schedule SET status = 'PENDING', updated_at = current_timestamp, updated_by = :updatedBy WHERE user_id = :userId AND (start_date BETWEEN :start_date AND :end_date OR end_date BETWEEN :start_date AND :end_date) AND status <> 'DONE' AND status <> 'REQUEST' AND status <> 'REJECTED' AND status <> 'APPROVE' AND status <> 'PENDING' AND is_delete <> 1;", nativeQuery = true)
        public void editStatusPendingScheduleByDateAdd(@Param("userId") Long userId, @Param("updatedBy") Long updatedBy,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

}
