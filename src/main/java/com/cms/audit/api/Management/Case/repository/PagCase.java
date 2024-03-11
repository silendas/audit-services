package com.cms.audit.api.Management.Case.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Case.models.Case;

public interface PagCase extends PagingAndSortingRepository<Case, Long> {
    Page<Case> findByNameContaining(String name, Pageable pageable);

}
