package com.cms.audit.api.AuditWorkingPaper.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;

@Repository
public interface PagAuditWorkingPaper extends PagingAndSortingRepository<AuditWorkingPaper, Long>{
    
    @Query(value = "SELECT * FROM audit_working_paper awp ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findAllWorkingPaper(Pageable pageable);


    @Query(value = "SELECT * FROM audit_working_paper awp WHERE awp.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperInDateRange(@Param("start_date") Date start_date,
        @Param("end_date") Date end_date, Pageable pageable);
}
