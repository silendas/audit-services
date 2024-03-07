package com.cms.audit.api.InspectionSchedule.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.dto.response.ScheduleInterface;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.Management.User.models.User;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // this is for select

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'REGULAR'", nativeQuery = true)
    public List<Schedule> findAllRowRegularSchedule();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'SPECIAL'", nativeQuery = true)
    public List<Schedule> findAllRowSpecialSchedule();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'REGULAR' AND u.status <> 'CLOSE' AND u.status <> 'REJECTED' AND u.is_delete = 0", nativeQuery = true)
    public List<Schedule> findAllScheduleForRegular();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.category = 'SPECIAL' AND u.status <> 'CLOSE' AND u.status <> 'REJECTED' AND u.is_delete = 0", nativeQuery = true)
    public List<Schedule> findAllScheduleForSpecial();

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.status <> 'CLOSE' ", nativeQuery = true)
    public List<Schedule> findAllScheduleByUserId(@Param("userId") Long id);

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND u.start_date BETWEEN :start_date AND :end_date AND u.end_date BETWEEN :start_date AND :end_date ", nativeQuery = true)
    public List<Schedule> findScheduleInDateRangeByUserId(@Param("userId") Long userId,
            @Param("category") String ucategory, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

    @Query(value = "SELECT DISTINCT u.id, u.user_id, u.branch_id, u.description, u.category, u.status, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.is_delete FROM inspection_schedule u, branch_office b, area_office q, region_office r WHERE u.branch_id = b.id AND b.area_id = q.id AND q.region_id = :regionId ;", nativeQuery = true)
    public List<ScheduleInterface> findOneScheduleByRegionId(@Param("regionId") Long regionId);

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.id = :scheduleId AND u.status <> 'CLOSE' ", nativeQuery = true)
    public Optional<Schedule> findOneScheduleById(@Param("scheduleId") Long scheduleId);

//     @Query(value = "SELECT * FROM inspection_schedule u WHERE u.id = :scheduleId AND u.status <> 'CLOSE' ", nativeQuery = true)
//     public Optional<Schedule> findOneByStartDate(@Param("scheduleId") Long scheduleId);

    @Query(value = "SELECT * FROM inspection_schedule u WHERE u.status = :status", nativeQuery = true)
    public List<Schedule> findOneScheduleByStatus(@Param("status") String scheduleId);

    // this is for update

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE inspection_schedule SET status = 'PENDING', updated_at = current_timestamp, updated_by = :updatedBy WHERE user_id = :userId AND start_date BETWEEN :start_date AND :end_date AND end_date BETWEEN :start_date AND :end_date AND status <> 'DONE' AND status <> 'CLOSE' AND status <> 'PENDING'", nativeQuery = true)
    public void editStatusPendingScheduleByDate(@Param("userId") Long userId, @Param("updatedBy") String updatedBy, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

    // this is for soft delete

    @Query(value = "UPDATE inspection_schedule SET is_delete = 1, updated_at = current_timestamp WHERE id = :userId", nativeQuery = true)
    public User softDelete(@Param("userId") Long userId);

}
