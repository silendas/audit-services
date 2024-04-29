package com.cms.audit.api.Management.Office.MainOffice.services;

import java.util.List;
import java.util.Date;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Office.MainOffice.dto.MainDTO;
import com.cms.audit.api.Management.Office.MainOffice.dto.response.MainInterface;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.MainOffice.repository.MainRepository;
import com.cms.audit.api.Management.Office.MainOffice.repository.PagMain;
import com.cms.audit.api.Management.Office.RegionOffice.services.RegionService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MainService {

    @Autowired
    private MainRepository mainRepository;

    @Autowired
    private PagMain pagMain;

    @Autowired
    private RegionService regionService;

    public GlobalResponse findAll(String name, int page, int size) {
        try {
            Page<Main> response = null;
            if(name != null){
                response = pagMain.findByNameContaining(name, PageRequest.of(page, size));
            } else {
                response = pagMain.findAllMain(PageRequest.of(page, size));
            }
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
            List<MainInterface> response = mainRepository.findSpecificMain();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .data(response)
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
            Optional<Main> response = mainRepository.findOneMainById(id);
            if(!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found").data(response)
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

    public GlobalResponse save(MainDTO mainDTO) {
        try {

            Main main = new Main(
                null,
                mainDTO.getName(),
                0,
                new Date(),
                new Date()
            );

            Main response = mainRepository.save(main);
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

    public GlobalResponse edit(MainDTO mainDTO, Long id) {
        try {

            Main mainGet = mainRepository.findById(id).get();

            Main main = new Main(
                id,
                mainDTO.getName(),
                0,
                mainGet.getCreated_at(),
                new Date()
            );

            Main response = mainRepository.save(main);
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

            Main mainGet = mainRepository.findById(id).get();

            GlobalResponse getRegion = regionService.findSpecificByMainId(id);
            if(getRegion.getData() != null) {
                return GlobalResponse
                    .builder()
                    .message("Cannot delete because relation")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            }

            Main main = new Main(
                mainGet.getId(),
                mainGet.getName(),
                1,
                mainGet.getCreated_at(),
                new Date()
            );

            mainRepository.save(main);
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
