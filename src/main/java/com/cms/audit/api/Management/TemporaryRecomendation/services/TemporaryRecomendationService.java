package com.cms.audit.api.Management.TemporaryRecomendation.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.TemporaryRecomendation.dto.TemporaryRecomendationDTO;
import com.cms.audit.api.Management.TemporaryRecomendation.models.TemporaryRecomendation;
import com.cms.audit.api.Management.TemporaryRecomendation.repository.TemporaryRecomendationRepository;
import com.cms.audit.api.Management.TemporaryRecomendation.response.Response;
import com.cms.audit.api.Management.TemporaryRecomendation.response.TemporaryRecomendationInterface;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TemporaryRecomendationService {

    @Autowired
    private TemporaryRecomendationRepository tempooraryRecomendationRepository;

    public Response findAll() {
        try {
            List<TemporaryRecomendationInterface> tempooraryRecomendationResponse = tempooraryRecomendationRepository.findAllTemporaryRecomendation();
            if (tempooraryRecomendationResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(tempooraryRecomendationResponse)
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
            List<TemporaryRecomendationInterface> tempooraryRecomendationResponse = tempooraryRecomendationRepository.findOneTemporaryRecomendationById(id);
            if (tempooraryRecomendationResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(tempooraryRecomendationResponse)
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

    public Response save(TemporaryRecomendationDTO tempooraryRecomendationDTO) {
        try {

            TemporaryRecomendation level = new TemporaryRecomendation(
                null,
                tempooraryRecomendationDTO.getName(),
                0,
                new Date(),
                new Date()
            );

            TemporaryRecomendation tempooraryRecomendationResponse = tempooraryRecomendationRepository.save(level);
            if (tempooraryRecomendationResponse == null) {
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

    public Response edit(TemporaryRecomendationDTO tempooraryRecomendationDTO) {
        try {

            TemporaryRecomendation levelGet = tempooraryRecomendationRepository.findById(tempooraryRecomendationDTO.getId()).get();

            TemporaryRecomendation level = new TemporaryRecomendation(
                tempooraryRecomendationDTO.getId(),
                tempooraryRecomendationDTO.getName(),
                tempooraryRecomendationDTO.getIs_delete(),
                levelGet.getCreated_at(),
                new Date()
            );

            TemporaryRecomendation tempooraryRecomendationResponse = tempooraryRecomendationRepository.save(level);
            if (tempooraryRecomendationResponse == null) {
                return Response
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(tempooraryRecomendationResponse)
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
