package com.cms.audit.api.Management.CaseCategory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;

@Repository
public interface PagCaseCategory extends PagingAndSortingRepository<CaseCategory, Long> {

    @Query(value = "SELECT * FROM case_category u WHERE u.name = :name AND u.is_delete <> 1", nativeQuery = true)
    Page<CaseCategory> findByNameContaining(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM case_category u WHERE u.cases_id = :id AND u.is_delete <> 1", nativeQuery = true)
    Page<CaseCategory> findByCases(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM case_category u WHERE u.is_delete = 0", nativeQuery = true)
    Page<CaseCategory> findAllCC(Pageable pageable);
}
