package com.cms.audit.api.Management.Case.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.Management.Case.models.Case;

public interface PagCase extends PagingAndSortingRepository<Case, Long> {

    @Query(value = "SELECT * FROM cases WHERE is_delete <> 1;", nativeQuery = true)
    Page<Case> findAllCases(Pageable pageable);

    @Query(value = "SELECT * FROM cases WHERE name LIKE %:name% AND is_delete <> 1;", nativeQuery = true)
    Page<Case> findAllCasesByName(@Param("name") String name, Pageable pageable);

    Page<Case> findByNameContaining(String name, Pageable pageable);

}
