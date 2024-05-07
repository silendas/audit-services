package com.cms.audit.api.Management.Position.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Position.models.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long>{

    @Query("SELECT p FROM Position p WHERE p.is_delete = 0")
    List<Position> findAllPosition();

}
