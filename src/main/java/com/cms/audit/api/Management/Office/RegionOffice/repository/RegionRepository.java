package com.cms.audit.api.Management.Office.RegionOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.response.RegionInterface;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{
    
    @Query(value = "SELECT u.id, u.name FROM region_office u", nativeQuery = true)
    public List<RegionInterface> findAllRegion();

    @Query(value = "SELECT u.id, u.name FROM region_office u WHERE u.main_id = :mainId", nativeQuery = true)
    public List<RegionInterface> findOneRegionByMainId(@Param("mainId") Long id);

    @Query(value = "SELECT u.id, u.name FROM region_office u WHERE u.id = :regionId", nativeQuery = true)
    public List<RegionInterface> findOneRegionById(@Param("regionId") Long id);

}
