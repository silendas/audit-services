package com.cms.audit.api.Management.Role.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.Role.response.RoleInterface;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    
    @Query(value = "SELECT u.id, u.name FROM role u", nativeQuery = true)
    public List<RoleInterface> findAllRole();

    @Query(value = "SELECT u.id, u.name FROM role u WHERE u.id = :roleId", nativeQuery = true)
    public List<RoleInterface> findOneRoleById(@Param("roleId") Long id);

}
