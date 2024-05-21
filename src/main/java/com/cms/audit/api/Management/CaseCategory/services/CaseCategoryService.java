package com.cms.audit.api.Management.CaseCategory.services;

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
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.dto.CaseCategoryDTO;
import com.cms.audit.api.Management.CaseCategory.dto.response.CaseCategoryInterface;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;
import com.cms.audit.api.Management.CaseCategory.repository.PagCaseCategory;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;

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

    public GlobalResponse findAll(Long caseId, String name, int page, int size, String code) {
        try {
             Specification<CaseCategory> spec = Specification
                    .where(new SpecificationFIlter<CaseCategory>().byNameLike(name))
                    .and(new SpecificationFIlter<CaseCategory>().getByCasesId(caseId))
                    .and(new SpecificationFIlter<CaseCategory>().caseCodeLike(code))
                    .and(new SpecificationFIlter<CaseCategory>().isNotDeleted())
                    .and(new SpecificationFIlter<CaseCategory>().orderByIdAsc());
            Page<CaseCategory> response = pagCaseCategory.findAll(spec, PageRequest.of(page, size));
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
            List<CaseCategoryInterface> response = caseCategoryRepository.findSpecificCaseCategory();
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
            Optional<CaseCategory> response = caseCategoryRepository.findOneCaseCategoryById(id);
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

    public GlobalResponse findOneByCasesId(Long id, int page, int size) {
        try {
            Optional<Case> set = caseRepository.findById(id);
            if (!set.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .build();
            }
            Page<CaseCategory> response = pagCaseCategory.findByCases(set.get().getId(), PageRequest.of(page, size));
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

    public GlobalResponse findSpecificByCasesId(Long id) {
        try {
            List<CaseCategoryInterface> response = caseCategoryRepository.findSpecificCaseCategoryByCases(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
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

    public GlobalResponse save(CaseCategoryDTO caseCategoryDTO) {
        try {
            if(caseCategoryDTO.getCase_id() == null) {
                return GlobalResponse.builder().errorMessage("Case id tidak boleh kosong").message("Case tidak boleh kosong").status(HttpStatus.BAD_REQUEST).build();
            } else if(caseCategoryDTO.getName() != null && caseCategoryDTO.getName() == "") {
                return GlobalResponse.builder().errorMessage("Name tidak boleh kosong").message("Case name tidak boleh kosong").status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Case> caseId = caseRepository.findById(caseCategoryDTO.getCase_id());
            if (!caseId.isPresent()) {
                return GlobalResponse.builder().message("Data case with id: "+caseCategoryDTO.getCase_id()+" not found").status(HttpStatus.BAD_REQUEST).build();
            }
            List<CaseCategory> check = caseCategoryRepository.findAll();
            for (CaseCategory caseCategory : check) {
                if (caseCategory.getName().equals(caseCategoryDTO.getName())) {
                    return GlobalResponse
                            .builder()
                            .errorMessage("Name harus unik")
                            .message("Name harus unik")
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
            }

            CaseCategory caseCategoryEntity = new CaseCategory(
                    null,
                    caseCategoryDTO.getName(),
                    0,
                    new Date(),
                    new Date(),
                    caseId.get());

            CaseCategory response = caseCategoryRepository.save(caseCategoryEntity);
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

    public GlobalResponse edit(CaseCategoryDTO caseCategoryDTO, Long id) {
        try {
            CaseCategory caseCategoryGet = caseCategoryRepository.findById(id).get();

            Optional<Case> caseId = caseRepository.findById(caseCategoryDTO.getCase_id());
            if (!caseId.isPresent()) {
                return GlobalResponse.builder().message("Data case not found").status(HttpStatus.BAD_REQUEST).build();
            }

            List<CaseCategory> check = caseCategoryRepository.findAll();
            for (CaseCategory caseCategory : check) {
                if (caseCategory.getName().equals(caseCategoryDTO.getName())) {
                    return GlobalResponse
                            .builder()
                            .errorMessage("Name harus unik")
                            .message("Name harus unik")
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
            }

            CaseCategory caseCategoryEntity = new CaseCategory(
                    id,
                    caseCategoryDTO.getName(),
                    0,
                    caseCategoryGet.getCreated_at(),
                    new Date(),
                    caseId.get());

            CaseCategory response = caseCategoryRepository.save(caseCategoryEntity);
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
