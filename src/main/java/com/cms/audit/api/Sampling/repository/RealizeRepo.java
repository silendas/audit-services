package com.cms.audit.api.Sampling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.RealizeSampling;

@Repository
public interface RealizeRepo extends JpaRepository<RealizeSampling, Long> {
    
    @Query("SELECT r FROM RealizeSampling r WHERE r.sampling.id = ?1")
    List<RealizeSampling> findAllBySamplingId(Long id);

}
