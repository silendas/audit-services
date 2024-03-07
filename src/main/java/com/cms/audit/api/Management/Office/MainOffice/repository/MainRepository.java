package com.cms.audit.api.Management.Office.MainOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;

@Repository
public interface MainRepository extends JpaRepository<Main, Long> {

    @Query(value = "SELECT * FROM main_office u WHERE is_delete <> 1", nativeQuery = true)
    public List<Main> findAllMain();

    @Query(value = "SELECT * FROM main_office u WHERE u.id = :mainId AND is_delete <> 1", nativeQuery = true)
    public List<Main> findOneMainById(@Param("mainId") Long id);

    @Query(value = "UPDATE main_office SET is_delete = 1, updated_at = current_timestamp WHERE id = :mainId", nativeQuery = true)
    public Branch softDelete(@Param("mainId") Long mainId);

}
