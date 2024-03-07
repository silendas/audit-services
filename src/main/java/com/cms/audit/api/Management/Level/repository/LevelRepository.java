package com.cms.audit.api.Management.Level.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Level.models.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    @Query(value = "SELECT * FROM level u", nativeQuery = true)
    public List<Level> findAllLevel();

    @Query(value = "SELECT * FROM level u WHERE u.id = :levelId", nativeQuery = true)
    public Optional<Level> findOneLevelById(@Param("levelId") Long id);

    @Query(value = "UPDATE level SET is_delete = 1, updated_at = current_timestamp WHERE id = :levelId", nativeQuery = true)
    public Level softDelete(@Param("levelId") Long levelId);

}
