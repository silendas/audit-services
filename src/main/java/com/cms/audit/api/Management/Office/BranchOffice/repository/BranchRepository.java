package com.cms.audit.api.Management.Office.BranchOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.response.BranchInterface;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query(value = "SELECT u.id, u.name FROM branch_office u", nativeQuery = true)
    public List<BranchInterface> findAllBranch();

    @Query(value = "SELECT u.id, u.name FROM branch_office u WHERE u.id = :branchId", nativeQuery = true)
    public List<BranchInterface> findOneBranchById(@Param("branchId") Long id);

    @Query(value = "SELECT u.id, u.name FROM branch_office u WHERE u.area_id = :areaId", nativeQuery = true)
    public List<BranchInterface> findOneBranchByRegionId(@Param("areaId") Long id);

}
