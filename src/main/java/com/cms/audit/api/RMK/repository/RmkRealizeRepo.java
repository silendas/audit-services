package com.cms.audit.api.RMK.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.RMK.model.RmkRealize;

@Repository
public interface RmkRealizeRepo extends JpaRepository<RmkRealize, Long>, JpaSpecificationExecutor<RmkRealize> {
    
    @Query("SELECT u FROM RmkRealize u WHERE u.rmkPlan = ?1")
    List<RmkRealize> findByRmkRealize(Long rmkPlanId);

}
