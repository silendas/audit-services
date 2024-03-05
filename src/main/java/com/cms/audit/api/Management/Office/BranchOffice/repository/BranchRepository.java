package com.cms.audit.api.Management.Office.BranchOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.BranchOffice.dto.response.BranchInterface;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    @Query(value = "SELECT u.id, u.name FROM branch_office u WHERE is_delete <> 1", nativeQuery = true)
    public List<BranchInterface> findAllBranch();

    @Query(value = "SELECT u.id, u.name FROM branch_office u WHERE u.id = :branchId AND is_delete <> 1", nativeQuery = true)
    public List<BranchInterface> findOneBranchById(@Param("branchId") Long branchid);

    @Query(value = "SELECT u.id, u.name FROM branch_office u WHERE u.area_id = :areaId AND is_delete <> 1", nativeQuery = true)
    public List<BranchInterface> findOneBranchByRegionId(@Param("areaId") Long areaId);

    @Query(value = "UPDATE branch_office SET is_delete = 1, updated_at = current_timestamp WHERE id = :branchId", nativeQuery = true)
    public Branch softDelete(@Param("branchId") Long branchId);

}
