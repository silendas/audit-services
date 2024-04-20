package com.cms.audit.api.AuditDailyReport.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditDailyReport.models.Revision;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long>{

    @Query(value = "SELECT * FROM revision u WHERE u.audit_daily_report_detail_id = :id AND u.is_delete <> 1 ORDER BY id DESC;",nativeQuery = true)
    List<Revision> findListByDetailId(@Param("id")Long id);
    
    @Query(value = "SELECT * FROM revision u WHERE u.audit_daily_report_detail_id = :id AND u.is_delete <> 1 ORDER BY id DESC LIMIT 1 ;",nativeQuery = true)
    Optional<Revision> findByDetailId(@Param("id")Long id);

}
