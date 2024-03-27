package com.cms.audit.api.Report.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;
import java.util.List;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;

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
                        
        @Query(value = "SELECT u.* FROM clarification u INNER JOIN users us ON u.user_id=us.id WHERE us.fullname LIKE '% :name %' AND u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ;", nativeQuery = true)
        Page<Clarification> findByAllFilter(@Param("name")String name, @Param("branchId") Long branchId, @Param("start_date") Date start_date,
        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
        Page<Clarification> findByUserInDateRange(@Param("id") Long id, @Param("start_date") Date start_date,
                        @Param("end_date") Date end_date, Pageable pageable);

        @Query(value = "SELECT * FROM clarification u WHERE u.user_id = :id;", nativeQuery = true)
        Page<Clarification> findByUserId(@Param("id") Long id, Pageable pageable);
}
