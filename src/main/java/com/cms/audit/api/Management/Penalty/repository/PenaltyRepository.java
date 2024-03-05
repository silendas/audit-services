package com.cms.audit.api.Management.Penalty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Penalty.dto.response.PenaltyInterface;
import com.cms.audit.api.Management.Penalty.models.Penalty;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    @Query(value = "SELECT u.id, u.name FROM penalty u", nativeQuery = true)
    public List<PenaltyInterface> findAllPenalty();

    @Query(value = "SELECT u.id, u.name FROM penalty u WHERE u.id = :penaltyId", nativeQuery = true)
    public List<PenaltyInterface> findOnePenaltyById(@Param("penaltyId") Long id);

    @Query(value = "UPDATE penalty SET is_delete = 1, updated_at = current_timestamp WHERE id = :penaltyId", nativeQuery = true)
    public Penalty softDelete(@Param("penaltyId") Long penaltyId);

}
