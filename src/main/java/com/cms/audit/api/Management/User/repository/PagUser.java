package com.cms.audit.api.Management.User.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.User.models.User;

public interface PagUser extends PagingAndSortingRepository<User, Long>{
    Page<User> findAll(Pageable pageable);

    Page<User> findByBranch(Branch branch, Pageable pageable);
    Page<User> findByArea(Area area, Pageable pageable);
    Page<User> findByRegion(Region region, Pageable pageable);
    Page<User> findByMain(Main main, Pageable pageable);
}
