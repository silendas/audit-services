package com.cms.audit.api.FollowUp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.FollowUp.models.FollowUp;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp,Long>{
    
    Optional<FollowUp> findByFileName(String fileName);
    
}
