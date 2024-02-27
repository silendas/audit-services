package com.cms.audit.api.Management.Office.AreaOffice.services;

import java.util.List;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Office.AreaOffice.dto.AreaDTO;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.AreaOffice.response.AreaInterface;
import com.cms.audit.api.Management.Office.AreaOffice.response.Response;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public Response findAll() {
        try {
            List<AreaInterface> areaResponse = areaRepository.findAllArea();
            if (areaResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(areaResponse)
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
            List<AreaInterface> areaResponse = areaRepository.findOneAreaById(id);
            if (areaResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(areaResponse)
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

    public Response findOneByRegionId(Long id) {
        try {
            List<AreaInterface> areaResponse = areaRepository.findOneAreaByRegionId(id);
            if (areaResponse.isEmpty()) {
                return Response
                        .builder()
                        .message("Not Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return Response
                    .builder()
                    .message("Success")
                    .data(areaResponse)
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

    public Response save(AreaDTO branchDTO) {
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

            Area areaResponse = areaRepository.save(level);
            if (areaResponse == null) {
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

    public Response edit(AreaDTO branchDTO) {
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

            Area areaResponse = areaRepository.save(level);
            if (areaResponse == null) {
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
