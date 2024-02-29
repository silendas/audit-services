package com.cms.audit.api.Management.Office.MainOffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.Office.MainOffice.dto.response.MainInterface;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;

@Repository
public interface MainRepository extends JpaRepository<Main, Long>{
    
    @Query(value = "SELECT u.id, u.name FROM main_office u", nativeQuery = true)
    public List<MainInterface> findAllMain();

    @Query(value = "SELECT u.id, u.name FROM main_office u WHERE u.id = :mainId", nativeQuery = true)
    public List<MainInterface> findOneMainById(@Param("mainId") Long id);

}
