package com.cms.audit.api.AuditWorkingPaper.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;


public interface AuditWorkingPaperRepository extends JpaRepository<AuditWorkingPaper, Long>{
    
}
