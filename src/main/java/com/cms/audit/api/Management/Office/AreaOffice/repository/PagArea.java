package com.cms.audit.api.Management.Office.AreaOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

public interface PagArea extends PagingAndSortingRepository<Area, Long> {

    Page<Area> findByNameContaining(String name, Pageable pageable);

    Page<Area> findByRegion(Region region, Pageable pageable);

}
