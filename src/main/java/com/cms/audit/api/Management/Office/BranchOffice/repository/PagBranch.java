package com.cms.audit.api.Management.Office.BranchOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;

@Repository
public interface PagBranch extends PagingAndSortingRepository<Branch, Long> {
    Page<Branch> findByNameContaining(String name, Pageable pageable);

    Page<Branch> findByArea(Area area, Pageable pageable);
}
