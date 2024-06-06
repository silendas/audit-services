package com.cms.audit.api.Dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cms.audit.api.FollowUp.models.FollowUp;

public interface FollowUpDashboardRepo extends JpaRepository<FollowUp, Long>, JpaSpecificationExecutor<FollowUp> {

}
