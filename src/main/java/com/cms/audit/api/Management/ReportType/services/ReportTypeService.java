package com.cms.audit.api.Management.ReportType.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.ReportType.dto.ReportTypeDTO;
import com.cms.audit.api.Management.ReportType.dto.response.ReportTypeInterface;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.ReportType.repository.ReportTypeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportTypeService {

    @Autowired
    private ReportTypeRepository reportTypeRepository;

    public GlobalResponse findAll() {
        try {
            List<ReportType> response = reportTypeRepository.findAllReportType();
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
            List<ReportTypeInterface> response = reportTypeRepository.findSpecificReportType();
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
            Optional<ReportType> response = reportTypeRepository.findOneReportTypeById(id);
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

    public GlobalResponse save(ReportTypeDTO dto) {
        try {

            if(dto.getName() == null || dto.getCode() == null || dto.getName() == "" || dto.getCode() == "") {
                return GlobalResponse
                        .builder()
                        .message("Data tidak boleh kosong")
                        .errorMessage("Data tidak boleh kosong")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }   

            ReportType reportType = new ReportType(
                    null,
                    dto.getName(),
                    dto.getCode(),
                    0,
                    new Date(),
                    new Date());

            ReportType response = reportTypeRepository.save(reportType);
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

    public GlobalResponse edit(ReportTypeDTO dto, Long id) {
        try {

            Optional<ReportType> getReport = reportTypeRepository.findById(id);

            ReportType reportType = getReport.get();
            reportType.setName(dto.getName());
            reportType.setCode(dto.getCode());
            reportType.setUpdated_at(new Date());
            reportTypeRepository.save(reportType);

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
            if(id == 1 || id == 2 || id == 3){
                return GlobalResponse.builder().message("Tidak dapat menghapus data default").status(HttpStatus.BAD_REQUEST).build();
            }
            ReportType getReport = reportTypeRepository.findById(id).get();

            ReportType reportType = new ReportType(
                    id,
                    getReport.getName(),
                    getReport.getCode(),
                    1,
                    getReport.getCreated_at(),
                    new Date());
            ReportType response = reportTypeRepository.save(reportType);
            if (response == null) {
                return GlobalResponse
                        .builder()
                        .message("Failed")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
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
