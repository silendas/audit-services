package com.cms.audit.api.Management.Office.AreaOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.AreaOffice.dto.response.AreaInterface;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query(value = "SELECT u.id, u.name FROM area_office u WHERE is_delete <> 1", nativeQuery = true)
    public List<AreaInterface> findAllArea();

    @Query(value = "SELECT u.id, u.name FROM area_office u WHERE u.region_id = :regionId WHERE is_delete <> 1", nativeQuery = true)
    public List<AreaInterface> findOneAreaByRegionId(@Param("regionId") Long id);

    @Query(value = "SELECT u.id, u.name FROM area_office u WHERE u.id = :areaId AND is_delete <> 1", nativeQuery = true)
    public List<AreaInterface> findOneAreaById(@Param("areaId") Long id);

    @Query(value = "UPDATE area_office SET is_delete = 1, updated_at = current_timestamp WHERE id = :areaId", nativeQuery = true)
    public Level softDelete(@Param("areaId") Long areaId);

}
