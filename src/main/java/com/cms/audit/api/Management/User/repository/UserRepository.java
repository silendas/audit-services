package com.cms.audit.api.Management.User.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.dto.response.DropDownUser;
import com.cms.audit.api.Management.User.dto.response.UserProfileInterface;
import com.cms.audit.api.Management.User.dto.response.UserRegionResponse;
import com.cms.audit.api.Management.User.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.is_delete <> 1 AND u.id <> 1")
    List<User> findAllUser();

    @Query(value="SELECT * FROM users u WHERE u.is_delete <> 1 AND u.id <> 1 AND u.level_id <> 2 AND u.level_id <> 1 AND u.level_id <> 4", nativeQuery=true)
    List<User> findAllUserIIfArea();

    @Query("SELECT u FROM User u where (u.email = ?1 or u.username = ?2) AND u.is_active = 1")
    Optional<User> findOneUsersByEmailOrUsername(String email, String username);

    @Query(value = "SELECT * FROM users u WHERE u.id IN (SELECT r.user_id FROM user_region r WHERE r.region_id = :id)", nativeQuery = true)
    List<User> findByRegion(@Param("id") Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByFullnameContainingIgnoreCase(String name);

    @Query(value = "SELECT * FROM users u WHERE u.fullname LIKE %:name% ORDER BY id DESC", nativeQuery = true)
    List<User> findByFullnameLike(@Param("name") String fullname);

    @Query(value = "SELECT u.id, u.role_id, u.level_id, u.nip, u.fullname, u.initial_name, u.email, u.username, u.password, u.is_active, u.created_at,u.updated_at FROM users u", nativeQuery = true)
    List<UserProfileInterface> findSpecificUser();

    @Query(value = "SELECT * FROM users u where u.main_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByMain(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u where u.region_id = :id;", nativeQuery = true)
    List<User> findUserByRegion(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u where u.branch_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByBranch(@Param("id") Long id);

    @Query(value = "SELECT * FROM users u where u.area_id = :id AND u.is_delete <> 1", nativeQuery = true)
    List<User> findUserByArea(@Param("id") Long id);

    @Query(value = "SELECT u.id, u.fullname, u.initial_name FROM users u where u.is_delete <> 1 AND u.id <> 1;", nativeQuery = true)
    List<DropDownUser> findDropDown();

    @Query(value = "SELECT r.user_id FROM users r WHERE r.region_id = :id", nativeQuery = true)
    List<UserRegionResponse> findUserRegionByRegionId(@Param("id") Long id);

    @Query(value = "SELECT u.id, u.fullname, u.initial_name FROM users u where u.id IN (SELECT r.user_id FROM user_region r WHERE r.region_id= :id ) AND u.is_delete <> 1;", nativeQuery = true)
    List<DropDownUser> findDropDownByRegion(@Param("id") Long id);

    @Query(value = "SELECT u.id, u.fullname, u.initial_name FROM users u where u.main_id = :id AND u.is_delete <> 1;", nativeQuery = true)
    List<DropDownUser> findDropDownByMain(@Param("id") Long id);

}
