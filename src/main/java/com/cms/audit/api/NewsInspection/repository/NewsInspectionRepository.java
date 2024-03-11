package com.cms.audit.api.NewsInspection.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.NewsInspection.models.NewsInspection;

@Repository
public interface NewsInspectionRepository extends JpaRepository<NewsInspection, Long> {
    @Query(value = "SELECT * FROM news_inspection u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    public List<NewsInspection> findBAPInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date);
}
