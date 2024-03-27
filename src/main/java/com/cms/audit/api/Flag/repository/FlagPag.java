package com.cms.audit.api.Flag.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Flag.model.Flag;

@Repository
public interface FlagPag extends PagingAndSortingRepository<Flag,Long>{
    
}
