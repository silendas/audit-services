package com.cms.audit.api.Dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.models.User;

@Repository
public interface UserDashboardRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
    
}
