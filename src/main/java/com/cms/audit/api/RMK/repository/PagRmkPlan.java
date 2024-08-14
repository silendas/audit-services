package com.cms.audit.api.RMK.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.RMK.model.RmkPlan;

@Repository
public interface PagRmkPlan extends PagingAndSortingRepository<RmkPlan, Long>, JpaSpecificationExecutor<RmkPlan> {
    
}
