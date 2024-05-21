package com.cms.audit.api.Management.Level.services;

import java.util.List;
import java.util.Date;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Level.dto.LevelDTO;
import com.cms.audit.api.Management.Level.dto.response.LevelInterface;
import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Level.repository.LevelRepository;
import com.cms.audit.api.Management.Level.repository.PagLevel;
import com.cms.audit.api.Management.ReportType.models.ReportType;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LevelService {

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private PagLevel pag;

    public GlobalResponse findAll(String name, int page, int size, String code) {
        try {
            Specification<Level> spec = Specification
                    .where(new SpecificationFIlter<Level>().byNameLike(name))
                    .and( new SpecificationFIlter<Level>().codeLike(code))
                    .and(new SpecificationFIlter<Level>().isNotDeleted())
                    .and(new SpecificationFIlter<Level>().orderByIdAsc());
            Page<Level> response = pag.findAll(spec, PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan").data(response)
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
            List<LevelInterface> response = levelRepository.findSpecificLevel();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan").data(response)
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
            Optional<Level> response = levelRepository.findOneLevelById(id);
            if(!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan").data(response)
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

    public GlobalResponse save(LevelDTO dto) {
        try {

            List<Level> check = levelRepository.findAllLevel();

            for (Level level : check) {
                if (level.getName().equals(dto.getName())) {
                    return GlobalResponse
                            .builder()
                            .errorMessage("Data sudah ada")
                            .message("Data sudah ada")
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
            }

            if(dto.getName() == null || dto.getCode() == null || dto.getName() == "" || dto.getCode() == ""){
                return GlobalResponse
                        .builder()
                        .message("Data tidak boleh kosong")
                        .errorMessage("Data tidak boleh kosong")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            
            Level level = new Level(
                    null,
                    dto.getName(),
                    dto.getCode(),
                    0,
                    new Date(),
                    new Date());

            Level response = levelRepository.save(level);
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

    public GlobalResponse edit(LevelDTO dto, Long id) {
        try {

            List<Level> check = levelRepository.findAllLevel();

            Optional<Level> checkId = levelRepository.findById(id);
            if(!dto.getName().equals(checkId.get().getName())){
                for (Level level : check) {
                    if (level.getName().equals(dto.getName())) {
                        return GlobalResponse
                                .builder()
                                .errorMessage("Data sudah ada")
                                .message("Data sudah ada")
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                }
            }

            if(id == 4 || id == 3 || id == 2 || id == 1){
                return GlobalResponse
                        .builder()
                        .message("karena masalah validasi jadi tidak boleh diubah")
                        .errorMessage("Level dengan id :" +id+ " tidak boleh diubah")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Level levelGet = levelRepository.findById(id).get();

            Level level = new Level(
                    id,
                    dto.getName(),
                    dto.getCode(),
                    0,
                    levelGet.getCreated_at(),
                    new Date());

            Level response = levelRepository.save(level);
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
            if(id == 4 || id == 3 || id == 2 || id == 1){
                return GlobalResponse
                        .builder()
                        .message("karena masalah validasi jadi tidak boleh dihapus")
                        .errorMessage("Level dengan id :" +id+ " tidak boleh diubah")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Level dto = levelRepository.findById(id).get();

            Level level = new Level(
                    id,
                    dto.getName(),
                    dto.getCode(),
                    1,
                    dto.getCreated_at(),
                    new Date());

            Level response = levelRepository.save(level);
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
