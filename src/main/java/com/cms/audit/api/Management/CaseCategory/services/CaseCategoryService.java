package com.cms.audit.api.Management.CaseCategory.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.dto.CaseCategoryDTO;
import com.cms.audit.api.Management.CaseCategory.dto.response.CaseCategoryInterface;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CaseCategoryService {

    @Autowired
    private CaseCategoryRepository caseCategoryRepository;

    public GlobalResponse findAll() {
        try {
            List<CaseCategoryInterface> response = caseCategoryRepository.findAllCaseCategory();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
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
            List<CaseCategoryInterface> response = caseCategoryRepository.findOneCaseCategoryById(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
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

    public GlobalResponse findOneByCasesId(Long id) {
        try {
            List<CaseCategoryInterface> response = caseCategoryRepository.findOneCaseCategoryByCasesId(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
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

    public GlobalResponse save(CaseCategoryDTO caseCategoryDTO) {
        try {

            Case caseId = Case
                    .builder()
                    .id(caseCategoryDTO.getCases_id())
                    .build();

            CaseCategory caseCategoryEntity = new CaseCategory(
                    null,
                    caseCategoryDTO.getName(),
                    0,
                    new Date(),
                    new Date(),
                    caseId);

            CaseCategory response = caseCategoryRepository.save(caseCategoryEntity);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
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

    public GlobalResponse edit(CaseCategoryDTO caseCategoryDTO, Long id) {
        try {
            CaseCategory caseCategoryGet = caseCategoryRepository.findById(id).get();

            Case caseId = Case
                    .builder()
                    .id(caseCategoryDTO.getCases_id())
                    .build();

            CaseCategory caseCategoryEntity = new CaseCategory(
                    id,
                    caseCategoryDTO.getName(),
                    0,
                    caseCategoryGet.getCreated_at(),
                    new Date(),
                    caseId
                    );

            CaseCategory response = caseCategoryRepository.save(caseCategoryEntity);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
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

            CaseCategory response = caseCategoryRepository.softDelete(id);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
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
