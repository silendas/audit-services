package com.cms.audit.api.AuditWorkingPaper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;

@Repository
public interface AuditWorkingPaperRepository extends JpaRepository<AuditWorkingPaper, Long>{

    Optional<AuditWorkingPaper> findByFilename(String filename);

    @Query(value = "SELECT * FROM audit_working_paper u WHERE u.schedule_id = :id ;", nativeQuery = true)
    Optional<AuditWorkingPaper> findByScheduleId(@Param("id") Long id);
    
}
