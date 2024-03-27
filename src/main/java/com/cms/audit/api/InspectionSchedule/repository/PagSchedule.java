package com.cms.audit.api.InspectionSchedule.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.models.Schedule;

@Repository
public interface PagSchedule extends PagingAndSortingRepository<Schedule, Long> {
        Page<Schedule> findAll(Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = :category AND u.status <> 'REJECTED' AND u.status <> 'CLOSE' AND u.is_delete <> 1 ORDER BY u.id DESC ", nativeQuery = true)
        Page<Schedule> findByCategoryInByOrderByIdDesc(@Param("category") String category, Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = :category AND u.start_date BETWEEN :start_date AND :end_date AND u.end_date BETWEEN :start_date AND :end_date AND u.is_delete <> 1 AND u.status <> 'CLOSE' ORDER BY u.start_date ASC;", nativeQuery = true)
        Page<Schedule> findScheduleByCategoryInDateRange(
                        @Param("category") String ucategory, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT u.id, u.user_id, u.branch_id, u.description, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.status, u.category, u.is_delete, u.updated_by, u.created_by, u.created_at, u.updated_at FROM inspection_schedule u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.category = :category AND u.is_delete <> 1 ORDER BY u.start_date ASC;", nativeQuery = true)
        Page<Schedule> findByRegionId(@Param("regionId") Long regionId, @Param("category") String ucategory,
                        Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category  AND u.is_delete <> 1 AND u.status <> 'CLOSE' ORDER BY u.start_date ASC;", nativeQuery = true)
        Page<Schedule> findAllScheduleByUserId(@Param("userId") Long id, @Param("category") String ucategory,
                        Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.branch_id = :branchId AND u.category = :category AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        Page<Schedule> findAllScheduleByBranchId(@Param("branchId") Long id,
                        @Param("category") String ucategory, Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = :category AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        Page<Schedule> findAllScheduleByDateRange(@Param("category") String ucategory,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u INNER JOIN users us ON u.user_id=us.id INNER JOIN branch_office bo ON u.branch_id=bo.id WHERE us.fullname LIKE '% :name %' AND bo.id = :branchId AND u.category = :category AND (u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date) AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
        Page<Schedule> findAllScheduleByAllFilter(@Param("name") String name, @Param("branchId") Long branchId,
                        @Param("category") String ucategory,
                        @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND u.start_date BETWEEN :start_date AND :end_date AND u.end_date BETWEEN :start_date AND :end_date AND u.is_delete <> 1 AND u.status <> 'CLOSE' ORDER BY u.start_date ASC;", nativeQuery = true)
        Page<Schedule> findScheduleInDateRangeByUserId(@Param("userId") Long userId,
                        @Param("category") String ucategory, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT u.id, u.user_id, u.branch_id, u.description, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.status, u.category, u.is_delete, u.updated_by, u.created_by, u.created_at, u.updated_at FROM inspection_schedule u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.category = :category AND u.start_date BETWEEN :start_date AND :end_date AND u.end_date BETWEEN :start_date AND :end_date AND u.is_delete <> 1 ORDER BY u.start_date ASC;", nativeQuery = true)
        Page<Schedule> findScheduleInDateRangeByRegionId(@Param("regionId") Long regionId,
                        @Param("category") String ucategory, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM inspection_schedule u WHERE u.status = :status AND u.is_delete <> 1 ORDER BY u.start_date ASC;", nativeQuery = true)
        Page<Schedule> findOneScheduleByStatus(@Param("status") String scheduleId, Pageable pageable);

}
