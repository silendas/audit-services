package com.cms.audit.api.Management.Office.AreaOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

public interface PagArea extends PagingAndSortingRepository<Area, Long> {

    Page<Area> findByNameContaining(String name, Pageable pageable);

    Page<Area> findByRegion(Region region, Pageable pageable);

    @Query(value = "SELECT * FROM area_office u WHERE u.region_id = :regionId AND u.is_delete <> 1", nativeQuery = true)
    Page<Area> findAreaByRegionId(@Param("regionId") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM area_office u WHERE u.is_delete <> 1", nativeQuery = true)
    Page<Area> findAllArea(Pageable pageable);

}
