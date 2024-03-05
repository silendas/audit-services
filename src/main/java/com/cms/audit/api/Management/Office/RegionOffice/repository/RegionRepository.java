package com.cms.audit.api.Management.Office.RegionOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.RegionOffice.dto.response.RegionInterface;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{
    
    @Query(value = "SELECT u.id, u.name FROM region_office u WHERE is_delete <> 1", nativeQuery = true)
    public List<RegionInterface> findAllRegion();

    @Query(value = "SELECT u.id, u.name FROM region_office u WHERE u.main_id = :mainId AND is_delete <> 1", nativeQuery = true)
    public List<RegionInterface> findOneRegionByMainId(@Param("mainId") Long id);

    @Query(value = "SELECT u.id, u.name FROM region_office u WHERE u.id = :regionId AND is_delete <> 1", nativeQuery = true)
    public List<RegionInterface> findOneRegionById(@Param("regionId") Long id);

        @Query(value = "UPDATE region_office SET is_delete = 1, updated_at = current_timestamp WHERE id = :regionId", nativeQuery = true)
    public Branch softDelete(@Param("regionId") Long regionId);

}
