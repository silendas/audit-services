package com.cms.audit.api.FollowUp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.FollowUp.models.FollowUp;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    @Query(value = "SELECT c.report_number, c.code, EXTRACT(YEAR FROM c.created_at) as created_year FROM follow_up c WHERE c.user_id = :userId AND c.is_delete = 0 ORDER BY c.id DESC LIMIT 1;", nativeQuery = true)
    Optional<NumberClarificationInterface> checkNumberFollowUp(@Param("userId") Long id);

    @Query(value = "SELECT c.* FROM follow_up c WHERE c.clarification_id = :id ;", nativeQuery = true)
    Optional<FollowUp> findByClId(@Param("id") Long id);

    Optional<FollowUp> findByFilename(String filename);

}
