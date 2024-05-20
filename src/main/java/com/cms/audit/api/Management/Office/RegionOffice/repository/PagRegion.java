package com.cms.audit.api.Management.Office.RegionOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

@Repository
public interface PagRegion extends PagingAndSortingRepository<Region, Long>, JpaSpecificationExecutor<Region> {
    Page<Region> findByNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM region_office u WHERE u.main_id = :mainId AND u.is_delete <> 1", nativeQuery = true)
    Page<Region> findRegionByMainId(@Param("mainId") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM region_office u WHERE u.is_delete <> 1", nativeQuery = true)
    Page<Region> findAllRegion(Pageable pageable);
}
