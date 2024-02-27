package com.cms.audit.api.Management.Case.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.response.CaseInterface;

@Repository
public interface CaseRepository extends JpaRepository<Case,Long>{
    
    @Query(value = "SELECT u.id, u.name, u.code FROM cases u", nativeQuery = true)
    public List<CaseInterface> findAllCase();

    @Query(value = "SELECT u.id, u.name, u.code FROM cases u WHERE u.id = :caseId", nativeQuery = true)
    public List<CaseInterface> findOneCaseById(@Param("caseId") Long id);

}
