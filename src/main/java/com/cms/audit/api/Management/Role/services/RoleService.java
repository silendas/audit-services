package com.cms.audit.api.Management.Role.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Role.dto.RoleDTO;
import com.cms.audit.api.Management.Role.dto.response.RoleInterface;
import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.Role.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public GlobalResponse findAll() {
        try {
            List<Role> response = roleRepository.findAllRole();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found").data(response)
                        .status(HttpStatus.OK)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menampilkan data")
                    .data(response)
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public GlobalResponse findSpecific() {
        try {
            List<RoleInterface> response = roleRepository.findSpecificRole();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found").data(response)
                        .status(HttpStatus.OK)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menampilkan data")
                    .data(response)
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public GlobalResponse findOne(Long id) {
        try {
            Optional<Role> response = roleRepository.findOneRoleById(id);
            if(!response.isPresent()) {
                return GlobalResponse.builder().message("Data not found")
                        .data(response)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menampilkan data")
                    .data(response)
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public GlobalResponse save(RoleDTO roleDTO) {
        try {

            Role role = new Role(
                    null,
                    roleDTO.getName(),
                    0,
                    new Date(),
                    new Date());

            Role response = roleRepository.save(role);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menambahkan data")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse edit(RoleDTO roleDTO, Long id) {
        try {
            if(id == 2 || id == 1){
                return GlobalResponse
                        .builder()
                        .message("karena masalah validasi jadi tidak boleh diubah")
                        .errorMessage("Role dengan id :" +id+ " tidak boleh diubah")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Role roleGet = roleRepository.findById(id).get();

            Role role = new Role(
                    id,
                    roleDTO.getName(),
                    0,
                    roleGet.getCreated_at(),
                    new Date());

            Role response = roleRepository.save(role);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil mengubah data")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse delete(Long id) {
        try {
            if(id == 2 || id == 1){
                return GlobalResponse
                        .builder()
                        .message("karena masalah validasi jadi tidak boleh dihapus")
                        .errorMessage("Role dengan id :" +id+ " tidak boleh diubah")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Role roleGet = roleRepository.findById(id).get();

            Role role = new Role(
                    id,
                    roleGet.getName(),
                    1,
                    roleGet.getCreated_at(),
                    new Date());

            Role response = roleRepository.save(role);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menghapus data")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
