package com.cms.audit.api.Management.Office.AreaOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.response.AreaInterface;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query(value = "SELECT u.id, u.name FROM area_office u", nativeQuery = true)
    public List<AreaInterface> findAllArea();

    @Query(value = "SELECT u.id, u.name FROM area_office u WHERE u.region_id = :regionId", nativeQuery = true)
    public List<AreaInterface> findOneAreaByRegionId(@Param("regionId") Long id);

    @Query(value = "SELECT u.id, u.name FROM area_office u WHERE u.id = :areaId", nativeQuery = true)
    public List<AreaInterface> findOneAreaById(@Param("areaId") Long id);

}
