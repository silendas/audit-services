package com.cms.audit.api.Management.TemporaryRecomendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.TemporaryRecomendation.models.TemporaryRecomendation;
import com.cms.audit.api.Management.TemporaryRecomendation.response.TemporaryRecomendationInterface;

@Repository
public interface TemporaryRecomendationRepository extends JpaRepository<TemporaryRecomendation, Long>{
    
    @Query(value = "SELECT u.id, u.name FROM temporary_recomendations u", nativeQuery = true)
    public List<TemporaryRecomendationInterface> findAllTemporaryRecomendation();

    @Query(value = "SELECT u.id, u.name FROM temporary_recomendations u WHERE u.id = :temporary_recomendationsId", nativeQuery = true)
    public List<TemporaryRecomendationInterface> findOneTemporaryRecomendationById(@Param("temporary_recomendationsId") Long id);

}
