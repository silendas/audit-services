package com.cms.audit.api.Report.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface PagReport extends PagingAndSortingRepository<Clarification, Long> {
        @Query(value = "SELECT * FROM clarification u ORDER BY u.id DESC ;", nativeQuery = true)
        Page<Clarification> findAllLHADetail(Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        Page<Clarification> findClarificationInDateRange(@Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.branch_id = :id ;", nativeQuery = true)
        Page<Clarification> findByBranchId(@Param("id") Long id, Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.branch_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        Page<Clarification> findByBranchInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);
                        
        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.user_id = :user_id AND u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ;", nativeQuery = true)
        Page<Clarification> findByAllFilter(@Param("user_id")Long user_id, @Param("region_id")Long regionId,@Param("branchId") Long branchId, @Param("start_date") Date start_date,
        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        Page<Clarification> findByUserInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id;", nativeQuery = true)
        Page<Clarification> findByUserId(@Param("id") Long id, Pageable pageable);
        
        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id ;", nativeQuery = true)
        Page<Clarification> findByRegionId(@Param("id") Long id, Pageable pageable);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.user_id = :userId ;", nativeQuery = true)
        Page<Clarification> findByRegionIdAndUser(@Param("id") Long id, @Param("userId")Long userId,Pageable pageable);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.branch_id = :branchId ;", nativeQuery = true)
        Page<Clarification> findByRegionIdAndBranch(@Param("id") Long id, @Param("branchId")Long branchId,Pageable pageable);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.created_at BETWEEN :start_date AND :end_date", nativeQuery = true)
        Page<Clarification> findByRegionIdAndDate(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);
}
