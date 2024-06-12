package com.cms.audit.api.Dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.FollowUp.models.FollowUp;

@Repository
public interface FollowUpDashboardRepo extends JpaRepository<FollowUp, Long>, JpaSpecificationExecutor<FollowUp> {

}
