package com.cms.audit.api.RMK.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.RMK.model.Clasification;

@Repository
public interface ClasificationRepo extends JpaRepository<Clasification, Long>, JpaSpecificationExecutor<Clasification> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
