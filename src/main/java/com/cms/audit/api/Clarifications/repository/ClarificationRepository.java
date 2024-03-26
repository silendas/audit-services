package com.cms.audit.api.Clarifications.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.Clarifications.models.Clarification;

import java.util.Date;
import java.util.List;

@Repository
public interface ClarificationRepository extends JpaRepository<Clarification, Long> {

    @Query(value = "SELECT c.report_number, c.code, EXTRACT(YEAR FROM c.created_at) as created_year FROM clarification c WHERE c.user_id = :userId ORDER BY c.id DESC LIMIT 1;", nativeQuery = true)
    Optional<NumberClarificationInterface> checkNumberClarification(@Param("userId") Long id);

    @Query(value = "SELECT * FROM clarification u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    public List<Clarification> findClarificationInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date);

    Optional<Clarification> findByFilename(String filename);

}
