package com.cms.audit.api.Management.Level.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Level.models.Level;

@Repository
public interface PagLevel extends PagingAndSortingRepository<Level, Long>, JpaSpecificationExecutor<Level>{
    
}
