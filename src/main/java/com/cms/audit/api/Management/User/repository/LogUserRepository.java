package com.cms.audit.api.Management.User.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.models.LogUser;

@Repository
public interface LogUserRepository extends JpaRepository<LogUser, Long>{
    @Query(value = "SELECT u.* FROM log_users u WHERE u.user_id = :id ; ",nativeQuery = true)
    public List<LogUser> findByUserId(@Param("id")Long id);
}
