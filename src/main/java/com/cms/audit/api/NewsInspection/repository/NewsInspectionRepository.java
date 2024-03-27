package com.cms.audit.api.NewsInspection.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.NewsInspection.models.NewsInspection;

@Repository
public interface NewsInspectionRepository extends JpaRepository<NewsInspection, Long> {

    @Query(value = "SELECT c.report_number, c.code, EXTRACT(YEAR FROM c.created_at) as created_year FROM news_inspection c WHERE c.user_id = :userId ORDER BY c.id DESC LIMIT 1;", nativeQuery = true)
    Optional<NumberClarificationInterface> checkNumberBAP(@Param("userId") Long id);

    Optional<NewsInspection> findByFileName(String fileName);
}
