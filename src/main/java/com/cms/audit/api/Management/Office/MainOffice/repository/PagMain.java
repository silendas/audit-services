package com.cms.audit.api.Management.Office.MainOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Office.MainOffice.models.Main;

public interface PagMain extends PagingAndSortingRepository<Main, Long>, JpaSpecificationExecutor<Main> {
    Page<Main> findByNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM main_office u WHERE u.is_delete <> 1", nativeQuery = true)
    Page<Main> findAllMain(Pageable pageable);

}
