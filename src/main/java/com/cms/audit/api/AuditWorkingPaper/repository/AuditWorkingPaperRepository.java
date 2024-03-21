package com.cms.audit.api.AuditWorkingPaper.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;

@Repository
public interface AuditWorkingPaperRepository extends JpaRepository<AuditWorkingPaper, Long>{

    Optional<AuditWorkingPaper> findByFileName(String fileName);
    
}
