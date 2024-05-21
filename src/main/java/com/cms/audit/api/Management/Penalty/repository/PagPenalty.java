package com.cms.audit.api.Management.Penalty.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Penalty.models.Penalty;

@Repository
public interface PagPenalty extends PagingAndSortingRepository<Penalty, Long>, JpaSpecificationExecutor<Penalty>{
    
}
