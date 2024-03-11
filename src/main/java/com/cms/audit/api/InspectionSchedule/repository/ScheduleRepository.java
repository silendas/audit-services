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

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'REGULAR' ORDER BY u.id DESC ", nativeQuery = true)
    public List<Schedule> findAllRowRegularSchedule();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'SPECIAL' ORDER BY u.id DESC ", nativeQuery = true)
    public List<Schedule> findAllRowSpecialSchedule();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'REGULAR' AND u.status <> 'REJECTED' AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
    public List<Schedule> findAllScheduleForRegular();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'SPECIAL' AND u.status <> 'REJECTED' AND u.is_delete = 0 ORDER BY u.id DESC ", nativeQuery = true)
    public List<Schedule> findAllScheduleForSpecial();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category ORDER BY u.id DESC ", nativeQuery = true)
    public List<Schedule> findAllScheduleByUserId(@Param("userId") Long id,  @Param("category") String ucategory);

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND u.start_date BETWEEN :start_date AND :end_date OR u.end_date BETWEEN :start_date AND :end_date", nativeQuery = true)
    public List<Schedule> findScheduleInDateRangeByUserId(@Param("userId") Long userId,
            @Param("category") String ucategory, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

    @Query(value = "SELECT u.id, u.user_id, u.branch_id, u.description, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.status, u.category, u.is_delete, u.updated_by, u.created_by, u.created_at, u.updated_at FROM inspection_schedule u INNER JOIN branch_office bo ON u.branch_id = bo.id INNER JOIN area_office ao ON bo.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId ;", nativeQuery = true)
    public List<Schedule> findOneScheduleByRegionId(@Param("regionId") Long regionId);

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.id = :scheduleId ", nativeQuery = true)
    public Optional<Schedule> findOneScheduleById(@Param("scheduleId") Long scheduleId);

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.status = :status", nativeQuery = true)
    public List<Schedule> findOneScheduleByStatus(@Param("status") String scheduleId);

    // this is for update

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE inspection_schedule SET status = 'PENDING', updated_at = current_timestamp, updated_by = :updatedBy WHERE user_id = :userId AND start_date BETWEEN :start_date AND :end_date OR end_date BETWEEN :start_date AND :end_date AND status <> 'DONE' AND status <> 'REJECTED' AND status <> 'NA' AND status <> 'PENDING'", nativeQuery = true)
    public void editStatusPendingScheduleByDate(@Param("userId") Long userId, @Param("updatedBy") String updatedBy, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

}
