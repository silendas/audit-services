package com.cms.audit.api.Management.Case.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Case.dto.CaseDTO;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.Case.response.CaseInterface;
import com.cms.audit.api.Management.Case.response.Response;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    public Response findAll() {
        try {
            List<CaseInterface> caseResponse = caseRepository.findAllCase();
            if (caseResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(caseResponse)
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public Response findOne(Long id) {
        try {
            List<CaseInterface> caseResponse = caseRepository.findOneCaseById(id);
            if (caseResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(caseResponse)
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    public Response save(CaseDTO caseDTO) {
        try {

            Case caseEntity = new Case(
                null,
                caseDTO.getName(),
                caseDTO.getCode(),
                0,
                new Date(),
                new Date()
            );

            Case caseResponse = caseRepository.save(caseEntity);
            if (caseResponse == null) {
                return Response
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public Response edit(CaseDTO caseDTO) {
        try {

            Case caseGet = caseRepository.findById(caseDTO.getId()).get();

            Case caseEntity = new Case(
                caseDTO.getId(),
                caseDTO.getName(),
                caseDTO.getCode(),
                0,
                caseGet.getCreated_at(),
                new Date()
            );

            Case caseResponse = caseRepository.save(caseEntity);
            if (caseResponse == null) {
                return Response
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return Response
                    .builder()
                    .message("Exception :" + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
