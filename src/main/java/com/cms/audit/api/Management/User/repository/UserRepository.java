package com.cms.audit.api.Management.User.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.dto.response.DropDownUser;
import com.cms.audit.api.Management.User.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u where u.email = ?1 or u.username = ?2")
    Optional<User> findOneUsersByEmailOrUsername(String email, String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u where u.main_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByMain(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u where u.region_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByRegion(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u where u.branch_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByBranch(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u where u.area_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByArea(@Param("id") Long id);

    @Query(value = "SELECT u.id, u.fullname, u.initial_name FROM users u where u.region_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<DropDownUser> findDropDownByRegion(@Param("id") Long id);

    @Query(value = "SELECT u.id, u.fullname, u.initial_name FROM users u where u.main_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<DropDownUser> findDropDownByMain(@Param("id") Long id);

}
