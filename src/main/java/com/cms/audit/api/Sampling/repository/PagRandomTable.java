package com.cms.audit.api.Sampling.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Sampling.model.RandomTable;

@Repository
public interface PagRandomTable extends PagingAndSortingRepository<RandomTable, Long>, JpaSpecificationExecutor<RandomTable> {
    
}
