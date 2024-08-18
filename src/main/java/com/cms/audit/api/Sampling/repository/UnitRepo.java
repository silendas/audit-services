package com.cms.audit.api.Sampling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.UnitSampling;

@Repository
public interface UnitRepo extends JpaRepository<UnitSampling, Long> {
    
    @Query(value = "SELECT * FROM unit_sampling WHERE sampling_id = ?1", nativeQuery = true)
    List<UnitSampling> findAllBySamplingId(Long sammplingId);

}
