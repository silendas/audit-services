package com.cms.audit.api.Management.User.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u where u.email = ?1 or u.username = ?2")
    Optional<User> findOneUsersByEmailOrUsername(String email, String username);

    Optional<User> findByEmail(String email);
}
