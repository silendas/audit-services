package com.cms.audit.api.FollowUp.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.FollowUp.models.FollowUp;

@Repository
public interface PagFollowup extends PagingAndSortingRepository<FollowUp, Long> {
    @Query(value = "SELECT u.* FROM follow_up u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<FollowUp> findFollowUpInNameAndDate(@Param("name")String name,@Param("start_date") Date start_date,@Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT u.* FROM follow_up u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% AND u.branch_id= :branchId AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<FollowUp> findFollowUpInAllFilter(@Param("name")String name,@Param("branchId") Long id,@Param("start_date") Date start_date,@Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT u.* FROM follow_up u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% AND u.branch_id= :branchId ", nativeQuery = true)
    Page<FollowUp> findFollowUpInNameAndBranch(@Param("name")String name, @Param("branchId") Long id,Pageable pageable);

    @Query(value = "SELECT u.* FROM follow_up u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE %:name% ", nativeQuery = true)
    Page<FollowUp> findFollowUpInName(@Param("name")String name, Pageable pageable);

    @Query(value = "SELECT * FROM follow_up u WHERE u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<FollowUp> findFollowUpInBranchAndDateRange(@Param("branchId") Long id,@Param("start_date") Date start_date,@Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT * FROM follow_up u WHERE u.branch_id = :branchId ", nativeQuery = true)
    Page<FollowUp> findFollowUpInBranch(@Param("branchId") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM follow_up u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<FollowUp> findFollowUpInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);
}
