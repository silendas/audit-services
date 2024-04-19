package com.cms.audit.api.Management.Office.AreaOffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.cms.audit.api.Management.Office.AreaOffice.dto.response.AreaInterface;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query(value = "SELECT * FROM area_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<Area> findAllArea();

    @Query(value = "SELECT * FROM area_office u WHERE u.region_id = :regionId AND u.is_delete <> 1", nativeQuery = true)
    public List<Area> findAreaByRegionId(@Param("regionId") Long id);

    @Query(value = "SELECT u.id,u.name FROM area_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<AreaInterface> findSpecificArea();

    @Query(value = "SELECT u.id,u.name FROM area_office u WHERE u.region_id = :regionId AND u.is_delete <> 1", nativeQuery = true)
    public List<AreaInterface> findSpecificAreaByRegionId(@Param("regionId") Long id);

    @Query(value = "SELECT * FROM area_office u WHERE u.id = :areaId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Area> findOneAreaById(@Param("areaId") Long id);

}
