package com.cms.audit.api.Management.Role.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Role.dto.response.RoleInterface;
import com.cms.audit.api.Management.Role.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    
    @Query(value = "SELECT * FROM role u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<Role> findAllRole();

    @Query(value = "SELECT u.id,u.name FROM role u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<RoleInterface> findSpecificRole();

    @Query(value = "SELECT * FROM role u WHERE u.id = :roleId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Role> findOneRoleById(@Param("roleId") Long id);

}
