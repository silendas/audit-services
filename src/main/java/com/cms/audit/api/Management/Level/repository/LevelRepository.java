package com.cms.audit.api.Management.Level.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Level.dto.response.LevelInterface;
import com.cms.audit.api.Management.Level.models.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    @Query(value = "SELECT u.id, u.name, u.code FROM level u", nativeQuery = true)
    public List<LevelInterface> findAllLevel();

    @Query(value = "SELECT u.id, u.name, u.code FROM level u WHERE u.id = :levelId", nativeQuery = true)
    public List<LevelInterface> findOneLevelById(@Param("levelId") Long id);

    @Query(value = "UPDATE level SET is_delete = 1, updated_at = current_timestamp WHERE id = :levelId", nativeQuery = true)
    public Level softDelete(@Param("levelId") Long levelId);

}
