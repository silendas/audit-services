package com.cms.audit.api.Management.User.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.User.models.User;
import java.util.List;


public interface PagUser extends PagingAndSortingRepository<User, Long>{
    @Query("SELECT u FROM User u WHERE u.is_delete <> 1")
    Page<User> findAll(Pageable pageable);

    Page<User> findByAreaId(List<Long> areaId,Pageable pageable);
    Page<User> findByBranchId(List<Long> branchId, Pageable pageable);
    Page<User> findByRegionId(List<Long> regionId,Pageable pageable);
    Page<User> findByMain(Main main, Pageable pageable);
}
