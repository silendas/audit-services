package com.cms.audit.api.Management.ReportType.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.Role.models.Role;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType,Long>{
    
    @Query(value = "UPDATE report_type SET is_delete = 1, updated_at = current_timestamp WHERE id = :rtId", nativeQuery = true)
    public ReportType softDelete(@Param("rtId") Long rtId);

}
