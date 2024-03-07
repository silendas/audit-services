package com.cms.audit.api.Management.Role.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Role.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    
    @Query(value = "UPDATE role SET is_delete = 1, updated_at = current_timestamp WHERE id = :roleId", nativeQuery = true)
    public Role softDelete(@Param("roleId") Long roleId);

}
