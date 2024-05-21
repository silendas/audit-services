package com.cms.audit.api.Management.Penalty.services;

import java.util.Date;
import java.util.List;
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
import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Penalty.dto.PenaltyDTO;
import com.cms.audit.api.Management.Penalty.dto.response.PenaltyInterface;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PagPenalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PenaltyService {

    @Autowired
    private PenaltyRepository PenaltyRepository;

    @Autowired
    private PagPenalty pag;

    public GlobalResponse findAll(String name, int page, int size) {
        try {
            Specification<Penalty> spec = Specification
                    .where(new SpecificationFIlter<Penalty>().byNameLike(name))
                    .and(new SpecificationFIlter<Penalty>().isNotDeleted())
                    .and(new SpecificationFIlter<Penalty>().orderByIdAsc());
            Page<Penalty> response = pag.findAll(spec, PageRequest.of(page, size));
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
            List<PenaltyInterface> response = PenaltyRepository.findSpecificPenalty();
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
            Optional<Penalty> response = PenaltyRepository.findOnePenaltyById(id);
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

    public GlobalResponse save(PenaltyDTO PenaltyDTO) {
        try {

            if(PenaltyDTO.getName() == null || PenaltyDTO.getName() == ""){
                return GlobalResponse.builder().message("Data tidak boleh kosong").errorMessage("Data tidak boleh kosong").status(HttpStatus.BAD_REQUEST).build();
            }

            List<Penalty> check = PenaltyRepository.findAllPenalty();

            for (Penalty penalty : check) {
                if (penalty.getName().equals(PenaltyDTO.getName())) {
                    return GlobalResponse
                            .builder()
                            .errorMessage("Data sudah ada")
                            .message("Data sudah ada")
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
            }

            Penalty Penalty = new Penalty(
                null,
                PenaltyDTO.getName(),
                0,
                new Date(),
                new Date()
            );

            Penalty response = PenaltyRepository.save(Penalty);

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

    public GlobalResponse edit(PenaltyDTO PenaltyDTO, Long id) {
        try {
            Penalty getPenalty = PenaltyRepository.findById(id).get();

            List<Penalty> check = PenaltyRepository.findAllPenalty();

            if(!getPenalty.getName().equals(PenaltyDTO.getName())){
                for (Penalty penalty : check) {
                    if (penalty.getName().equals(PenaltyDTO.getName())) {
                        return GlobalResponse
                                .builder()
                                .errorMessage("Data sudah ada")
                                .message("Data sudah ada")
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                }
            }

            Penalty penalty = new Penalty(
                id,
                PenaltyDTO.getName(),
                0,
                getPenalty.getCreated_at(),
                new Date()
            );

            Penalty response = PenaltyRepository.save(penalty);

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
            Penalty getPenalty = PenaltyRepository.findById(id).get();

            Penalty penalty = new Penalty(
                id,
                getPenalty.getName(),
                1,
                getPenalty.getCreated_at(),
                new Date()
            );
            Penalty response = PenaltyRepository.save(penalty);

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
