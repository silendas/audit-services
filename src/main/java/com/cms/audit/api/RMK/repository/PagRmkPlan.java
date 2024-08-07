package com.cms.audit.api.RMK.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.RMK.model.RmkPlan;

public interface PagRmkPlan extends PagingAndSortingRepository<RmkPlan, Long>, JpaSpecificationExecutor<RmkPlan> {
    
}
