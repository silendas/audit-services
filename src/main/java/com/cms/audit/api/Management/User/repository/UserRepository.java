package com.cms.audit.api.Management.User.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.User.dto.response.UserProfileInterface;
import com.cms.audit.api.Management.User.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u where u.email = ?1 or u.username = ?2")
    Optional<User> findOneUsersByEmailOrUsername(String email, String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findAllUserProfile();

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.main_id = :mainId AND u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findOneUserProfileByMainId(@Param("mainId") Long id);

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.region_id = :regionId AND u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findOneUserProfileByRegionId(@Param("regionId") Long id);

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.area_id = :areaId AND u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findOneUserProfileByAreaId(@Param("areaId") Long id);

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.branch_id = :branchId AND u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findOneUserProfileByBranchId(@Param("branchId") Long id);

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.level_id = :levelId AND u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findOneUserProfileByLevelId(@Param("levelId") Long id);

    @Query(value = "SELECT u.id , u.nip , u.full_name , u.initial_name, u.email, u.username, u.password FROM users u WHERE u.id = :userId AND u.is_active = 1", nativeQuery = true)
    public List<UserProfileInterface> findOneUserProfileById(@Param("userId") Long id);

    // this is update

    @Query(value = "UPDATE users SET is_delete = 1, is_active = 0, updated_at = current_timestamp WHERE id = :userId", nativeQuery = true)
    public User softDelete(@Param("userId") Long userId);


    //this is for change password
    @Query(value = "UPDATE users SET password = :newPassword, updated_at = current_timestamp WHERE id = :userId", nativeQuery = true)
    public User changePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
}
