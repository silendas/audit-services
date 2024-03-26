package com.cms.audit.api.Management.Office.BranchOffice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;

@Repository
public interface PagBranch extends PagingAndSortingRepository<Branch, Long> {
    Page<Branch> findByNameContaining(String name, Pageable pageable);

    Page<Branch> findByArea(Area area, Pageable pageable);

    @Query(value = "SELECT * FROM branch_office u WHERE u.area_id = :areaId AND u.is_delete <> 1", nativeQuery = true)
    Page<Branch> findBranchByAreaId(@Param("areaId") Long areaId, Pageable pageable);
    
    @Query(value = "SELECT * FROM branch_office u WHERE u.is_delete <> 1", nativeQuery = true)
    Page<Branch> findAllBranch(Pageable pageable);
}
