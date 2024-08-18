package com.cms.audit.api.Sampling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.BranchSampling;

@Repository
public interface SamplingRepository extends JpaRepository<BranchSampling, Long>, JpaSpecificationExecutor<BranchSampling> {
    
}
