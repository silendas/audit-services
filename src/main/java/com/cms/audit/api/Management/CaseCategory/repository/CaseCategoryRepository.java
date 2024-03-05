package com.cms.audit.api.Management.CaseCategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.dto.response.CaseCategoryInterface;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;

@Repository
public interface CaseCategoryRepository extends JpaRepository<CaseCategory, Long> {

    @Query(value = "SELECT u.id, u.name FROM case_category u", nativeQuery = true)
    public List<CaseCategoryInterface> findAllCaseCategory();

    @Query(value = "SELECT u.id, u.name FROM case_category u WHERE u.cases_id = :casesId", nativeQuery = true)
    public List<CaseCategoryInterface> findOneCaseCategoryByCasesId(@Param("casesId") Long id);

    @Query(value = "SELECT u.id, u.name FROM case_category u WHERE u.id = :case_categoryId", nativeQuery = true)
    public List<CaseCategoryInterface> findOneCaseCategoryById(@Param("case_categoryId") Long id);

    @Query(value = "UPDATE case_category SET is_delete = 1, updated_at = current_timestamp WHERE id = :casesId", nativeQuery = true)
    public CaseCategory softDelete(@Param("casesId") Long casesId);

}
