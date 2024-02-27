package com.cms.audit.api.Management.PermanentRecomendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.PermanentRecomendation.models.PermanentRecomendation;
import com.cms.audit.api.Management.PermanentRecomendation.response.PermanentRecomendationInterface;

@Repository
public interface PermanentRecomendationRepository extends JpaRepository<PermanentRecomendation, Long>{
    
    @Query(value = "SELECT u.id, u.name FROM permanent_recomendations u", nativeQuery = true)
    public List<PermanentRecomendationInterface> findAllPermanentRecomendation();

    @Query(value = "SELECT u.id, u.name FROM permanent_recomendations u WHERE u.id = :permanent_recomendationsId", nativeQuery = true)
    public List<PermanentRecomendationInterface> findOnePermanentRecomendationById(@Param("permanent_recomendationsId") Long id);

}
