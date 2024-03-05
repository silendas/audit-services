package com.cms.audit.api.InspectionSchedule.repository;

import java.util.Date;
import java.util.List;

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
    // this is for insert

    @Query(value = "INSERT INTO inspection_schedule (category, created_at, created_by, description, end_date, end_date_realization, start_date, start_date_realization, status, updated_at, updated_by, branch_id, user_id) VALUES (':category', current_timestamp, :createdBy,':description',':endDate', null, ':startDate', null, ':status', current_timestamp, null, :branchId, :userId)", nativeQuery = true)
    public Schedule insertSchedule(@Param("category") String category, @Param("createdBy") String createdBy,
            @Param("description") String description, @Param("endDate") Date endDate,
            @Param("startDate") Date startDate, @Param("status") String status, @Param("branchId") Long branchId,
            @Param("userId") Long userId);

    // this is for select

    @Query(value = "SELECT DISTINCT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category, b.name FROM inspection_schedule u, branch_office b WHERE u.category = 'REGULAR' AND u.branch_id = b.id", nativeQuery = true)
    public List<ScheduleInterface> findAllRowRegularSchedule();

    @Query(value = "SELECT DISTINCT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category, b.name FROM inspection_schedule u, branch_office b WHERE u.category = 'SPECIAL' AND u.branch_id = b.id", nativeQuery = true)
    public List<ScheduleInterface> findAllRowSpecialSchedule();

    @Query(value = "SELECT DISTINCT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category, b.name FROM inspection_schedule u, branch_office b WHERE u.category = 'REGULAR' AND u.branch_id = b.id AND u.status <> 'CLOSE' AND is_delete <> 1", nativeQuery = true)
    public List<ScheduleInterface> findAllScheduleForRegular();

    @Query(value = "SELECT DISTINCT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category, b.name FROM inspection_schedule u, branch_office b WHERE u.category = 'SPECIAL' AND u.branch_id = b.id AND u.status <> 'CLOSE'AND is_delete <> 1", nativeQuery = true)
    public List<ScheduleInterface> findAllScheduleForSpecial();

    @Query(value = "SELECT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category, b.name FROM inspection_schedule u, branch_office b WHERE u.user_id = :userId AND u.branch_id = b.id AND u.status <> 'CLOSE' AND is_delete <> 1", nativeQuery = true)
    public List<ScheduleInterface> findAllScheduleByUserId(@Param("userId") Long id);

    @Query(value = "SELECT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category FROM inspection_schedule u WHERE u.user_id = :userId AND u.category = :category AND u.start_date BETWEEN :start_date AND :end_date AND u.end_date BETWEEN :start_date AND :end_date AND is_delete <> 1", nativeQuery = true)
    public List<ScheduleInterface> findScheduleInDateRangeByUserId(@Param("userId") Integer userId, @Param("category") String ucategory, @Param("start_date") Date start_date, @Param("end_date") Date end_date);

    @Query(value = "SELECT DISTINCT u.id, u.category, u.descrption, u.end_date, u.end_date_realization, u.start_date, u.start_date_realization, u.status, u.branch_id, u.user_id FROM inspection_schedule u, branch_office b, area_office q, region_office r WHERE u.branch_id = b.id AND b.area_id = q.id AND q.region_id = :regonId AND is_delete <> 1;", nativeQuery = true)
    public List<ScheduleInterface> findOneScheduleByRegionId(@Param("regonId") Long regionId);

    @Query(value = "SELECT u.id, u.start_date, u.end_date, u.start_date_realization, u.end_date_realization, u.description, u.status, u.category FROM inspection_schedule u WHERE u.id = :scheduleId AND u.status <> 'CLOSE' AND is_delete <> 1", nativeQuery = true)
    public List<ScheduleInterface> findOneScheduleById(@Param("scheduleId") Long scheduleId);

    // this is for update

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE inspection_schedule SET status = 'PENDING', updated_at = current_timestamp WHERE user_id = :userId AND start_date BETWEEN :start_date AND :end_date AND end_date BETWEEN :start_date AND :end_date AND status <> 'DONE' AND status <> 'CLOSE' AND status <> 'PENDING'", nativeQuery = true)
    public void editStatusPendingScheduleByDate(@Param("userId") Long userId, @Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

    @Query(value = "UPDATE inspection_schedule SET status = 'PROGRESS', updated_at = current_timestamp WHERE id = :scheduleId", nativeQuery = true)
    public Schedule editStatusProgresScheduleById(@Param("scheduleId") Long scheduleId);

    @Query(value = "UPDATE inspection_schedule SET status = 'TODO', updated_at = current_timestamp WHERE id = :scheduleId", nativeQuery = true)
    public Schedule editStatusTodoScheduleById(@Param("scheduleId") Long scheduleId);

    @Query(value = "UPDATE inspection_schedule SET status = 'DONE', updated_at = current_timestamp WHERE id = :scheduleId", nativeQuery = true)
    public Schedule editStatusDoneScheduleById(@Param("scheduleId") Long scheduleId);

    @Query(value = "UPDATE inspection_schedule SET status = 'CLOSE', updated_at = current_timestamp WHERE id = :scheduleId", nativeQuery = true)
    public Schedule editStatusCloseScheduleById(@Param("scheduleId") Long scheduleId);

    @Query(value = "UPDATE inspection_schedule SET status = 'PENDING', updated_at = current_timestamp WHERE id = :scheduleId", nativeQuery = true)
    public Schedule editStatusPendingScheduleById(@Param("scheduleId") Long scheduleId);

    // this is for soft delete

    @Query(value = "UPDATE inspection_schedule SET is_delete = 1, updated_at = current_timestamp WHERE id = :userId", nativeQuery = true)
    public User softDelete(@Param("userId") Long userId);

}
