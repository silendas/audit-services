package com.cms.audit.api.Management.Office.RegionOffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.RegionOffice.dto.response.RegionInterface;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query(value = "SELECT * FROM region_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<Region> findAllRegion();

    @Query(value = "SELECT * FROM region_office u WHERE u.main_id = :mainId AND u.is_delete <> 1", nativeQuery = true)
    public List<Region> findRegionByMainId(@Param("mainId") Long id);
    
    @Query(value = "SELECT u.id,u.name FROM region_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<RegionInterface> findSpecificRegion();

    @Query(value = "SELECT u.id,u.name FROM region_office u WHERE u.main_id = :mainId AND u.is_delete <> 1", nativeQuery = true)
    public List<RegionInterface> findSpecificRegionByMainId(@Param("mainId") Long id);

    @Query(value = "SELECT * FROM region_office u WHERE u.id = :regionId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Region> findOneRegionById(@Param("regionId") Long id);

}
