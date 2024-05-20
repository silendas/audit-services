package com.cms.audit.api.Management.Office.BranchOffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.dto.response.BranchInterface;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long>, JpaSpecificationExecutor<Branch> {

    @Query(value = "SELECT * FROM branch_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<Branch> findAllBranch();

    public List<Branch> findByNameContainingIgnoreCase(String name);

    @Query(value = "SELECT u.id,u.name FROM branch_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<BranchInterface> findSpecificBranch();

    @Query(value = "SELECT * FROM branch_office u WHERE u.id = :branchId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Branch> findOneBranchById(@Param("branchId") Long branchid);

    @Query(value = "SELECT * FROM branch_office u WHERE u.area_id = :areaId AND u.is_delete <> 1", nativeQuery = true)
    public List<Branch> findBranchByAreaId(@Param("areaId") Long areaId);

    @Query(value = "SELECT u.id,u.name FROM branch_office u WHERE u.id = :id AND u.is_delete <> 1", nativeQuery = true)
    public Optional<BranchInterface> findSpecificBranchById(@Param("id") Long id);

    @Query(value = "SELECT u.id,u.name FROM branch_office u WHERE u.area_id = :areaId AND u.is_delete <> 1", nativeQuery = true)
    public List<BranchInterface> findSpecificBranchByAreaId(@Param("areaId") Long areaId);

    @Query(value = "SELECT u.id, u.name FROM branch_office u INNER JOIN area_office ao ON u.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.is_delete <> 1;", nativeQuery = true)
    public List<BranchInterface> findSpecificBranchByRegionId(@Param("regionId") Long regionId);

    @Query(value = "SELECT u.* FROM branch_office u INNER JOIN area_office ao ON u.area_id = ao.id INNER JOIN region_office ro ON ao.region_id = ro.id WHERE ro.id = :regionId AND u.is_delete <> 1;", nativeQuery = true)
    public List<Branch> findBranchByRegionId(@Param("regionId") Long regionId);

}
