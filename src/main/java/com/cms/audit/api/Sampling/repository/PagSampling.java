package com.cms.audit.api.Sampling.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.BranchSampling;

@Repository
public interface PagSampling extends PagingAndSortingRepository<BranchSampling, Long>, JpaSpecificationExecutor<BranchSampling> {
    
}
