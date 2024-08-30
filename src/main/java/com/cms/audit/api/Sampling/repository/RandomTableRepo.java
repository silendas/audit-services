package com.cms.audit.api.Sampling.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.RandomTable;

@Repository
public interface RandomTableRepo extends JpaRepository<RandomTable, Long>, JpaSpecificationExecutor<RandomTable> {

    // Check if a RandomTable exists for the same branch in the same month and year
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END FROM RandomTable rt " +
           "WHERE rt.branch.id = :branchId " +
           "AND rt.created_at BETWEEN :startDate AND :endDate " +
           "AND rt.is_delete = 0")
    boolean existsByBranchAndCreatedAtBetween(@Param("branchId") Long branchId,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);

    // Check if a RandomTable exists for the same branch in the same month and year, excluding the current ID
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END FROM RandomTable rt " +
           "WHERE rt.branch.id = :branchId " +
           "AND rt.created_at BETWEEN :startDate AND :endDate " +
           "AND rt.id <> :id " +
           "AND rt.is_delete = 0")
    boolean existsByBranchAndCreatedAtBetweenAndIdNot(@Param("branchId") Long branchId,
                                                      @Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate,
                                                      @Param("id") Long id);
}
