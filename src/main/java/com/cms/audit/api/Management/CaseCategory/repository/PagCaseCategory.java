package com.cms.audit.api.Management.CaseCategory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;

public interface PagCaseCategory extends PagingAndSortingRepository<CaseCategory, Long> {
    Page<CaseCategory> findByNameContaining(String name, Pageable pageable);

    Page<CaseCategory> findByCases(Case cases, Pageable pageable);
}
