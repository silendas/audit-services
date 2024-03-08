package com.cms.audit.api.Management.Office.MainOffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.dto.response.MainInterface;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;

@Repository
public interface MainRepository extends JpaRepository<Main, Long> {

    @Query(value = "SELECT * FROM main_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<Main> findAllMain();

    @Query(value = "SELECT u.id,u.name FROM main_office u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<MainInterface> findSpecificMain();

    @Query(value = "SELECT * FROM main_office u WHERE u.id = :mainId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<Main> findOneMainById(@Param("mainId") Long id);

}
