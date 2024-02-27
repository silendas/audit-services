package com.cms.audit.api.Management.Penalty.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Penalty.dto.PenaltyDTO;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;
import com.cms.audit.api.Management.Penalty.response.PenaltyInterface;
import com.cms.audit.api.Management.Penalty.response.Response;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PenaltyService {

    @Autowired
    private PenaltyRepository PenaltyRepository;

    public Response findAll() {
        try {
            List<PenaltyInterface> PenaltyResponse = PenaltyRepository.findAllPenalty();
            if (PenaltyResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(PenaltyResponse)
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
            List<PenaltyInterface> PenaltyResponse = PenaltyRepository.findOnePenaltyById(id);
            if (PenaltyResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(PenaltyResponse)
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

    public Response save(PenaltyDTO PenaltyDTO) {
        try {

            Penalty Penalty = new Penalty(
                null,
                PenaltyDTO.getName(),
                new Date(),
                new Date()
            );

            Penalty PenaltyResponse = PenaltyRepository.save(Penalty);
            if (PenaltyResponse == null) {
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

    public Response edit(PenaltyDTO PenaltyDTO) {
        try {

            Penalty PenaltyGet = PenaltyRepository.findById(PenaltyDTO.getId()).get();

            Penalty Penalty = new Penalty(
                PenaltyDTO.getId(),
                PenaltyDTO.getName(),
                PenaltyGet.getCreated_at(),
                new Date()
            );

            Penalty PenaltyResponse = PenaltyRepository.save(Penalty);
            if (PenaltyResponse == null) {
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
