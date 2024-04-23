package com.cms.audit.api.Management.Level.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Level.dto.response.LevelInterface;
import com.cms.audit.api.Management.Level.models.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    @Query(value = "SELECT * FROM level u WHERE u.is_delete <> 1 ORDER BY u.code ASC", nativeQuery = true)
    public List<Level> findAllLevel();

    @Query(value = "SELECT u.id,u.name,u.code FROM level u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<LevelInterface> findSpecificLevel();

    @Query(value = "SELECT * FROM level u WHERE u.id = :levelId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Level> findOneLevelById(@Param("levelId") Long id);

}
