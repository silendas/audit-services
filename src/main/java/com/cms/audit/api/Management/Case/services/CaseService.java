package com.cms.audit.api.Management.Case.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Case.dto.CaseDTO;
import com.cms.audit.api.Management.Case.dto.response.CaseInterface;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.Case.repository.PagCase;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private CaseCategoryRepository caseCategoryRepository;

    @Autowired
    private PagCase pagCase;

    public GlobalResponse findAll(String name, int page, int size) {
        try {
            Page<Case> response = null;
            if(name != null){
                response = pagCase.findAllCasesByName(name, PageRequest.of(page, size));
            }else{
                response = pagCase.findAllCases(PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .data(response).status(HttpStatus.OK)
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
            List<CaseInterface> response = caseRepository.findSpecificCase();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .data(response).status(HttpStatus.OK)
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
            Optional<Case> response = caseRepository.findOneCaseById(id);
            if (!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .data(response).status(HttpStatus.OK)
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

    public GlobalResponse save(CaseDTO caseDTO) {
        try {

            Case caseEntity = new Case(
                    null,
                    caseDTO.getName(),
                    caseDTO.getCode(),
                    0,
                    new Date(),
                    new Date());

            Case response = caseRepository.save(caseEntity);
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

    public GlobalResponse edit(CaseDTO caseDTO, Long id) {
        try {
            Case caseGet = caseRepository.findById(id).get();
            if (caseGet == null) {
                return GlobalResponse
                        .builder()
                        .message("Case with id :" + id + " no found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Case caseEntity = new Case(
                    id,
                    caseDTO.getName(),
                    caseDTO.getCode(),
                    0,
                    caseGet.getCreated_at(),
                    new Date());

            Case response = caseRepository.save(caseEntity);
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
            Case caseGet = caseRepository.findById(id).get();

            List<CaseCategory> check = caseCategoryRepository.findOneCaseCategoryByCasesId(id);
            if (!check.isEmpty()) {
                return GlobalResponse.builder().message("Tidak bisa menghapus karena relasi tabel")
                        .status(HttpStatus.BAD_REQUEST).build();
            }

            Case caseEntity = new Case(
                    id,
                    caseGet.getName(),
                    caseGet.getCode(),
                    1,
                    caseGet.getCreated_at(),
                    new Date());

            caseRepository.save(caseEntity);

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
