package com.cms.audit.api.Clarifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface ClarificationRepository extends JpaRepository<Clarification, Long>{
    
}
