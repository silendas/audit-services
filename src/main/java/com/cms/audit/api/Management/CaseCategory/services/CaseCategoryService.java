package com.cms.audit.api.Management.CaseCategory.services;

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
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.dto.CaseCategoryDTO;
import com.cms.audit.api.Management.CaseCategory.dto.response.CaseCategoryInterface;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;
import com.cms.audit.api.Management.CaseCategory.repository.PagCaseCategory;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CaseCategoryService {

    @Autowired
    private CaseCategoryRepository caseCategoryRepository;

    @Autowired
    private PagCaseCategory pagCaseCategory;

    @Autowired
    private CaseRepository caseRepository;

    public GlobalResponse findAll(Long caseId, String name, int page, int size) {
        try {
            if (caseId != null) {
                return findOneByCasesId(caseId, page, size);
            }
            Page<CaseCategory> response = null;
            if (name != null) {
                response = pagCaseCategory.findByNameContaining(name, PageRequest.of(page, size));
            } else {
                response = pagCaseCategory.findAllCC(PageRequest.of(page, size));
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

    public GlobalResponse findSpecific() {
        try {
            List<CaseCategoryInterface> response = caseCategoryRepository.findSpecificCaseCategory();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found").data(response)
                        .status(HttpStatus.OK)
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
            Optional<CaseCategory> response = caseCategoryRepository.findOneCaseCategoryById(id);
            if(!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found").data(response)
                        .status(HttpStatus.BAD_REQUEST)
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

    public GlobalResponse findOneByCasesId(Long id, int page, int size) {
        try {
            Optional<Case> set = caseRepository.findById(id);
            if (!set.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .build();
            }
            Page<CaseCategory> response = pagCaseCategory.findByCases(set.get().getId(), PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found").data(response)
                        .status(HttpStatus.OK)
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

    public GlobalResponse findSpecificByCasesId(Long id) {
        try {
            List<CaseCategoryInterface> response = caseCategoryRepository.findSpecificCaseCategoryByCases(id);
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

            Optional<Case> caseId = caseRepository.findById(caseCategoryDTO.getCase_id());
            if (!caseId.isPresent()) {
                return GlobalResponse.builder().message("Data case with id: "+caseCategoryDTO.getCase_id()+" not found").status(HttpStatus.BAD_REQUEST).build();
            }

            CaseCategory caseCategoryEntity = new CaseCategory(
                    null,
                    caseCategoryDTO.getName(),
                    0,
                    new Date(),
                    new Date(),
                    caseId.get());

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

            Optional<Case> caseId = caseRepository.findById(caseCategoryDTO.getCase_id());
            if (!caseId.isPresent()) {
                return GlobalResponse.builder().message("Data case not found").status(HttpStatus.BAD_REQUEST).build();
            }

            CaseCategory caseCategoryEntity = new CaseCategory(
                    id,
                    caseCategoryDTO.getName(),
                    0,
                    caseCategoryGet.getCreated_at(),
                    new Date(),
                    caseId.get());

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
            CaseCategory caseCategoryGet = caseCategoryRepository.findById(id).get();

            Case caseId = Case
                    .builder()
                    .id(caseCategoryGet.getCases().getId())
                    .build();

            CaseCategory caseCategoryEntity = new CaseCategory(
                    id,
                    caseCategoryGet.getName(),
                    1,
                    caseCategoryGet.getCreated_at(),
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

}
