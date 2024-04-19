package com.cms.audit.api.Management.CaseCategory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.CaseCategory.dto.response.CaseCategoryInterface;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;

@Repository
public interface CaseCategoryRepository extends JpaRepository<CaseCategory, Long> {

    @Query(value = "SELECT * FROM case_category u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<CaseCategory> findAllCaseCategory();

    @Query(value = "SELECT u.id,u.name FROM case_category u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<CaseCategoryInterface> findSpecificCaseCategory();

    @Query(value = "SELECT u.id,u.name FROM case_category u WHERE u.cases_id = :casesId AND u.is_delete <> 1", nativeQuery = true)
    public List<CaseCategoryInterface> findSpecificCaseCategoryByCases(@Param("casesId") Long id);

    @Query(value = "SELECT * FROM case_category u WHERE u.cases_id = :casesId AND u.is_delete <> 1", nativeQuery = true)
    public List<CaseCategory> findOneCaseCategoryByCasesId(@Param("casesId") Long id);

    @Query(value = "SELECT * FROM case_category u WHERE u.id = :case_categoryId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<CaseCategory> findOneCaseCategoryById(@Param("case_categoryId") Long id);

}
