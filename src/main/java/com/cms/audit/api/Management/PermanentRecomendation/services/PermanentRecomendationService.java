package com.cms.audit.api.Management.PermanentRecomendation.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.PermanentRecomendation.dto.PermanentRecomendationDTO;
import com.cms.audit.api.Management.PermanentRecomendation.models.PermanentRecomendation;
import com.cms.audit.api.Management.PermanentRecomendation.repository.PermanentRecomendationRepository;
import com.cms.audit.api.Management.PermanentRecomendation.response.PermanentRecomendationInterface;
import com.cms.audit.api.Management.PermanentRecomendation.response.Response;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PermanentRecomendationService {

    @Autowired
    private PermanentRecomendationRepository permanentRecomendationRepository;

    public Response findAll() {
        try {
            List<PermanentRecomendationInterface> permanentRecomendationResponse = permanentRecomendationRepository.findAllPermanentRecomendation();
            if (permanentRecomendationResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(permanentRecomendationResponse)
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
            List<PermanentRecomendationInterface> permanentRecomendationResponse = permanentRecomendationRepository.findOnePermanentRecomendationById(id);
            if (permanentRecomendationResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(permanentRecomendationResponse)
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

    public Response save(PermanentRecomendationDTO permanentRecomendationDTO) {
        try {

            PermanentRecomendation level = new PermanentRecomendation(
                null,
                permanentRecomendationDTO.getName(),
                0,
                new Date(),
                new Date()
            );

            PermanentRecomendation permanentRecomendationResponse = permanentRecomendationRepository.save(level);
            if (permanentRecomendationResponse == null) {
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

    public Response edit(PermanentRecomendationDTO permanentRecomendationDTO) {
        try {

            PermanentRecomendation levelGet = permanentRecomendationRepository.findById(permanentRecomendationDTO.getId()).get();

            PermanentRecomendation level = new PermanentRecomendation(
                permanentRecomendationDTO.getId(),
                permanentRecomendationDTO.getName(),
                permanentRecomendationDTO.getIs_delete(),
                levelGet.getCreated_at(),
                new Date()
            );

            PermanentRecomendation permanentRecomendationResponse = permanentRecomendationRepository.save(level);
            if (permanentRecomendationResponse == null) {
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
