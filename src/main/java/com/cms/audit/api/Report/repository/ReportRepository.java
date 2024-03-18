package com.cms.audit.api.Report.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface ReportRepository extends JpaRepository<Clarification,Long> {
        @Query(value = "SELECT * FROM clarification u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    List<Clarification> findClarificationInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date);
}
