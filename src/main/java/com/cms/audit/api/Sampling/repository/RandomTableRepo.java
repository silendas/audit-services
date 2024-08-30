package com.cms.audit.api.Sampling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.RandomTable;

@Repository
public interface RandomTableRepo extends JpaRepository<RandomTable, Long>, JpaSpecificationExecutor<RandomTable> {

    // Check if a RandomTable exists for the same branch in the current month and year
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END FROM RandomTable rt " +
           "WHERE rt.branch.id = :branchId " +
           "AND rt.created_at BETWEEN " +
           "DATE_TRUNC('month', CURRENT_DATE) AND " +
           "DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day' " +
           "AND rt.is_delete = 0")
    boolean existsByBranchInCurrentMonth(@Param("branchId") Long branchId);

    // Check if a RandomTable exists for the same branch in the current month and year, excluding the current ID
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END FROM RandomTable rt " +
           "WHERE rt.branch.id = :branchId " +
           "AND rt.created_at BETWEEN " +
           "DATE_TRUNC('month', CURRENT_DATE) AND " +
           "DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day' " +
           "AND rt.id <> :id " +
           "AND rt.is_delete = 0")
    boolean existsByBranchInCurrentMonthAndIdNot(@Param("branchId") Long branchId, @Param("id") Long id);
}
