package com.cms.audit.api.Management.Level.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Level.response.LevelInterface;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long>{
    
    @Query(value = "SELECT u.id, u.name, u.code FROM level u", nativeQuery = true)
    public List<LevelInterface> findAllLevel();

    @Query(value = "SELECT u.id, u.name, u.code FROM level u WHERE u.id = :levelId", nativeQuery = true)
    public List<LevelInterface> findOneLevelById(@Param("levelId") Long id);

}
