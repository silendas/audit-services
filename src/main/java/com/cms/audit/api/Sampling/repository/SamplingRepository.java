package com.cms.audit.api.Sampling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.Sampling;

@Repository
public interface SamplingRepository extends JpaRepository<Sampling, Long> {
    
}
