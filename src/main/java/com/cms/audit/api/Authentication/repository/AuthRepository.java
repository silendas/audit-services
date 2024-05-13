package com.cms.audit.api.Authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.models.User;


@Repository
public interface AuthRepository extends JpaRepository<User, Long>{
    @Query(value = "SELECT u.* FROM users u where (u.email = :email or u.username = :username) AND u.is_delete <> 1 AND u.is_active <> 0", nativeQuery = true)
    Optional<User> findOneUsersByEmailOrUsername(@Param("email") String email, @Param("username") String username);
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.is_delete <> 1")
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.is_delete <> 1")
    Optional<User> findByUsername(String username);
}