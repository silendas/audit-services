package com.cms.audit.api.Flag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Flag.model.Flag;
import java.util.Optional;

@Repository
public interface FlagRepo extends JpaRepository<Flag,Long>{

    @Query(name = "SELECT * FROM flag u WHERE u.audit_daily_report_id = :id")
    Optional<Flag> findOneByAuditDailyReportId(@Param("id") Long id);

    @Query(name = "SELECT * FROM flag u WHERE u.clarification_id = :id")
    Optional<Flag> findOneByClarificationId(@Param("id") Long id);
}
