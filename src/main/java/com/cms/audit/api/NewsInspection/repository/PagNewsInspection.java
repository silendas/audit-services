package com.cms.audit.api.NewsInspection.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.NewsInspection.models.NewsInspection;

@Repository
public interface PagNewsInspection extends PagingAndSortingRepository<NewsInspection, Long>, JpaSpecificationExecutor<NewsInspection> {
}
