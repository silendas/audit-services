package com.cms.audit.api.Report.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface ReportRepository extends JpaRepository<Clarification, Long> {
        @Query(value = "SELECT * FROM clarification u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        List<Clarification> findClarificationInDateRange(@Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        // @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id ;", nativeQuery = true)
        // List<Clarification> findByRegionId(@Param("id") Long id);

        @Query(value = "SELECT * FROM clarification u WHERE u.branch_id = :id ;", nativeQuery = true)
        List<Clarification> findByBranchId(@Param("id") Long id);

        @Query(value = "SELECT * FROM clarification u WHERE u.branch_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        List<Clarification> findByBranchInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM clarification u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE '% :name %' AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        List<Clarification> findByNameInDateRange(@Param("name") String name, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE '% :name %'", nativeQuery = true)
        List<Clarification> findByFullname(@Param("name") String name);

        // @Query(value = "SELECT u.* FROM clarification u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% AND u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ;", nativeQuery = true)
        // List<Clarification> findByAllFilter(@Param("name") String name, @Param("branchId") Long branchId,
        //                 @Param("start_date") Date start_date,
        //                 @Param("end_date") Date end_date);

        // @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.created_at BETWEEN :start_date AND :end_date", nativeQuery = true)
        // List<Clarification> findByRegionIdAndDate(@Param("id") Long id, @Param("start_date") Date start_date,
        //                 @Param("end_date") Date end_date);

        // @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        // List<Clarification> findByUserInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
        //                 @Param("end_date") Date end_date);

        // @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id;", nativeQuery = true)
        // List<Clarification> findByUserId(@Param("id") Long id);

        @Query(value = "SELECT * FROM clarification u ORDER BY u.id DESC ;", nativeQuery = true)
        List<Clarification> findAllLHADetail();

        // @Query(value = "SELECT * FROM clarification u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        // List<Clarification> findClarificationInDateRange(@Param("start_date") Date start_date,
        //                 @Param("end_date") Date end_date);

        // @Query(value = "SELECT * FROM clarification u WHERE u.branch_id = :id ;", nativeQuery = true)
        // List<Clarification> findByBranchId(@Param("id") Long id);

        // @Query(value = "SELECT * FROM clarification u WHERE u.branch_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        // List<Clarification> findByBranchInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
        //                 @Param("end_date") Date end_date);
                        
        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.user_id = :user_id AND u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ;", nativeQuery = true)
        List<Clarification> findByAllFilters(@Param("user_id")Long user_id, @Param("regionId")Long regionId,@Param("branchId") Long branchId, @Param("start_date") Date start_date,@Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        List<Clarification> findByUserInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);

        @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id;", nativeQuery = true)
        List<Clarification> findByUserId(@Param("id") Long id);
        
        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id ;", nativeQuery = true)
        List<Clarification> findByRegionId(@Param("id") Long id);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.user_id = :userId ;", nativeQuery = true)
        List<Clarification> findByRegionIdAndUser(@Param("id") Long id, @Param("userId")Long userId);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.branch_id = :branchId ;", nativeQuery = true)
        List<Clarification> findByRegionIdAndBranch(@Param("id") Long id, @Param("branchId")Long branchId);

        @Query(value = "SELECT u.* FROM clarification u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :id AND u.created_at BETWEEN :start_date AND :end_date", nativeQuery = true)
        List<Clarification> findByRegionIdAndDate(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date);
}
