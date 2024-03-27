package com.cms.audit.api.Management.Penalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Penalty.dto.response.PenaltyInterface;
import com.cms.audit.api.Management.Penalty.models.Penalty;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    @Query(value = "SELECT * FROM penalty u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<Penalty> findAllPenalty();

    @Query(value = "SELECT u.id,u.name FROM penalty u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<PenaltyInterface> findSpecificPenalty();

    @Query(value = "SELECT * FROM penalty u WHERE u.id = :penaltyId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Penalty> findOnePenaltyById(@Param("penaltyId") Long id);

}
