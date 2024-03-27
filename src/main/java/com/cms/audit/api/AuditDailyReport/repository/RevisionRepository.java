package com.cms.audit.api.AuditDailyReport.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.Revision;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long>{
    
    @Query(value = "SELECT * FROM revision u WHERE u.audit_daily_report_detail_id = :id ORDER BY id DESC LIMIT 1 ;",nativeQuery = true)
    Optional<Revision> findByDetailId(@Param("id")Long id);

}
