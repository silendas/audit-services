package com.cms.audit.api.RMK.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.RMK.model.Clasification;

@Repository
public interface PagClasification extends PagingAndSortingRepository<Clasification, Long>, JpaSpecificationExecutor<Clasification> {
    
}
