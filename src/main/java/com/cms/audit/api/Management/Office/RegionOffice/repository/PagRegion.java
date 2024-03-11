package com.cms.audit.api.Management.Office.RegionOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

public interface PagRegion extends PagingAndSortingRepository<Region, Long> {
    Page<Region> findByNameContaining(String name, Pageable pageable);

    Page<Region> findByMain(Main main, Pageable pageable);
}
