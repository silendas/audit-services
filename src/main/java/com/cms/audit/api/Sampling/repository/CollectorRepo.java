package com.cms.audit.api.Sampling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.CollectorSampling;

@Repository
public interface CollectorRepo extends JpaRepository<CollectorSampling, Long>, JpaSpecificationExecutor<CollectorSampling>{


    @Query("SELECT c FROM CollectorSampling c WHERE c.branchSampling.id = ?1")
    CollectorSampling findAllBySamplingId(Long sammplingId);
    
}
