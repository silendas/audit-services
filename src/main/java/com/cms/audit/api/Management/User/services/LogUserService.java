package com.cms.audit.api.Management.User.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.models.LogUser;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.LogUserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LogUserService {

    @Autowired
    private LogUserRepository repository;

    public ResponseEntity<Object> getLogByUserId(Long id) {
        try {
            List<LogUser> getLog = new ArrayList<>();
            if(id!=null){
                getLog = repository.findByUserId(id);
            }else{
                getLog = repository.findAll();
            }
            GlobalResponse response = GlobalResponse.builder().data(getLog)
                    .message("Data berhasil ditampilkan")
                    .status(HttpStatus.OK).build();
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    null);
        } catch (Exception e) {
            return ResponseEntittyHandler.errorResponse("Terjadi kesalahan", "Err: "+e, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<Object> getLogById(Long id) {
        try {
            Optional<LogUser> getLog = repository.findById(id);
            if(!getLog.isPresent()){
                return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan", HttpStatus.BAD_REQUEST);
            }
            GlobalResponse response = GlobalResponse.builder().data(getLog)
                    .message("Data berhasil ditampilkan")
                    .status(HttpStatus.OK).build();
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    null);
        } catch (Exception e) {
            return ResponseEntittyHandler.errorResponse("Terjadi kesalahan", "Err: "+e, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public void insertAuto(User dto) {

        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LogUser setLog = new LogUser();
        setLog.setUser(dto);
        setLog.setAreaId(dto.getAreaId());
        setLog.setBranchId(dto.getBranchId());
        setLog.setRegionId(dto.getRegionId());
        setLog.setMain(dto.getMain());
        setLog.setEmail(dto.getEmail());
        setLog.setFullname(dto.getFullname());
        setLog.setInitial_name(dto.getInitial_name());
        setLog.setIs_active(dto.getIs_active());
        setLog.setIs_delete(dto.getIs_delete());
        setLog.setLevel(dto.getLevel());
        setLog.setNip(dto.getNip());
        setLog.setPassword(dto.getPassword());
        setLog.setRole(dto.getRole());
        setLog.setUsername(dto.getUsername());
        setLog.setCreated_at(new Date());
        setLog.setCreated_by(getUser.getId());
        repository.save(setLog);
        // return GlobalResponse.builder().message("Data berhasil
        // ditambahkan").status(HttpStatus.OK).build();
    }

}
