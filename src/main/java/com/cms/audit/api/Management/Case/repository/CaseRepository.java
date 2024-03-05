package com.cms.audit.api.Management.Case.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.Management.Case.dto.response.CaseInterface;
import com.cms.audit.api.Management.Case.models.Case;

@Repository
public interface CaseRepository extends JpaRepository<Case,Long>{
    
    @Query(value = "SELECT u.id, u.name, u.code FROM cases u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<CaseInterface> findAllCase();

    @Query(value = "SELECT u.id, u.name, u.code FROM cases u WHERE u.id = :caseId AND u.is_delete <> 1", nativeQuery = true)
    public List<CaseInterface> findOneCaseById(@Param("caseId") Long id);

    @Query(value = "UPDATE cases SET is_delete = 1, updated_at = current_timestamp WHERE id = :casesId", nativeQuery = true)
    public Case softDelete(@Param("casesId") Long casesId);

}
