package com.cms.audit.api.Dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface ClarificationDashboardRepo extends JpaRepository<Clarification, Long>, JpaSpecificationExecutor<Clarification> {
    
}
