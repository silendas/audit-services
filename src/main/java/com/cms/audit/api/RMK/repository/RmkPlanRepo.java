package com.cms.audit.api.RMK.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.RMK.model.RmkPlan;

@Repository
public interface RmkPlanRepo extends JpaRepository<RmkPlan, Long>, JpaSpecificationExecutor<RmkPlan> {
    
}
