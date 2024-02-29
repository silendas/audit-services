package com.cms.audit.api.Management.Office.AreaOffice.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Office.AreaOffice.dto.AreaDTO;
import com.cms.audit.api.Management.Office.AreaOffice.dto.response.AreaInterface;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.common.response.GlobalResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public GlobalResponse findAll() {
        try {
            List<AreaInterface> response = areaRepository.findAllArea();
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
            List<AreaInterface> response = areaRepository.findOneAreaById(id);
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

    public GlobalResponse findOneByRegionId(Long id) {
        try {
            List<AreaInterface> response = areaRepository.findOneAreaByRegionId(id);
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

    public GlobalResponse save(AreaDTO branchDTO) {
        try {

            Region regionId = Region.builder()
                    .id(branchDTO.getRegion_id())
                    .build();

            Area level = new Area(
                    null,
                    branchDTO.getName(),
                    new Date(),
                    new Date(),
                    regionId);

            Area response = areaRepository.save(level);
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

    public GlobalResponse edit(AreaDTO branchDTO) {
        try {

            Area levelGet = areaRepository.findById(branchDTO.getId()).get();

            Region regionId = Region.builder()
                    .id(branchDTO.getRegion_id())
                    .build();

            Area level = new Area(
                    branchDTO.getId(),
                    branchDTO.getName(),
                    levelGet.getCreated_at(),
                    new Date(),
                    regionId);

            Area response = areaRepository.save(level);
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
