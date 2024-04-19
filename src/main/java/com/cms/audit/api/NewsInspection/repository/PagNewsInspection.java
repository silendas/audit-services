package com.cms.audit.api.NewsInspection.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.NewsInspection.models.NewsInspection;

@Repository
public interface PagNewsInspection extends PagingAndSortingRepository<NewsInspection, Long> {

    Page<NewsInspection> findAll(Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u WHERE u.user_id = :userId ", nativeQuery = true)
    Page<NewsInspection> findBAPInUserid(@Param("userId")Long id, Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE %:name% AND u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<NewsInspection> findBAPInAllFilter(@Param("name") String name, @Param("branchId")Long id,@Param("start_date") Date start_date,@Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE %:name% AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<NewsInspection> findBAPInNameAndDate(@Param("name") String name, @Param("start_date") Date start_date,@Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE %:name% ", nativeQuery = true)
    Page<NewsInspection> findBAPInName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u INNER JOIN users us ON u.user_id = us.id WHERE us.fullname LIKE %:name% AND u.branch_id = :branchId ", nativeQuery = true)
    Page<NewsInspection> findBAPInNameAndBranch(@Param("name") String name, @Param("branchId")Long id,Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u WHERE u.branch_id = :branchId AND u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<NewsInspection> findBAPInBranchAndDate(@Param("branchId")Long id,@Param("start_date") Date start_date,@Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u WHERE u.branch_id = :branchId ", nativeQuery = true)
    Page<NewsInspection> findBAPInBranch(@Param("branchId")Long id, Pageable pageable);

    @Query(value = "SELECT u.* FROM news_inspection u WHERE u.user_id = :branchId ", nativeQuery = true)
    Page<NewsInspection> findBAPInUser(@Param("branchId")Long id, Pageable pageable);

    @Query(value = "SELECT * FROM news_inspection u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<NewsInspection> findBAPInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);
}
