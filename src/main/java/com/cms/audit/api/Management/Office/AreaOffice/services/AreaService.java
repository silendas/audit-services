package com.cms.audit.api.Management.Office.AreaOffice.services;

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
import com.cms.audit.api.Management.Office.AreaOffice.dto.AreaDTO;
import com.cms.audit.api.Management.Office.AreaOffice.dto.response.AreaInterface;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.AreaOffice.repository.PagArea;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private PagArea pagArea;

    @Autowired
    private BranchService branchService;

    @Autowired
    private RegionRepository regionRepository;

    public GlobalResponse findAll(String name, int page, int size, Long regionId) {
        try {
            Page<Area> response;
            if (name != null) {
                response = pagArea.findByNameContaining(name, PageRequest.of(page, size));
            } else if (regionId != null) {
                response = pagArea.findAreaByRegionId(regionId, PageRequest.of(page, size));
            } else {
                response = pagArea.findAllArea(PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(response)
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
            List<AreaInterface> response = areaRepository.findSpecificArea();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(response)
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
            Optional<Area> response = areaRepository.findOneAreaById(id);
            if (!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public GlobalResponse findOneByRegionId(Long id, int page, int size) {
        try {
            Optional<Region> setRegion = regionRepository.findById(id);
            if (!setRegion.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Region with id:" +setRegion.get().getId()+" is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Page<Area> response = pagArea.findByRegion(setRegion.get(), PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public GlobalResponse findSpecificByRegionId(Long id) {
        try {
            List<AreaInterface> response = areaRepository.findSpecificAreaByRegionId(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public GlobalResponse save(AreaDTO dto) {
        try {

            Optional<Region> regionId = regionRepository.findById(dto.getRegion_id());
            if (!regionId.isPresent()) {
                return GlobalResponse.builder().message("Data region not found").status(HttpStatus.BAD_REQUEST).build();
            }

            Area area = new Area(
                    null,
                    dto.getName(),
                    new Date(),
                    new Date(),
                    0,
                    regionId.get());

            Area response = areaRepository.save(area);
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

    public GlobalResponse edit(AreaDTO dto, Long id) {
        try {

            Area areaGet = areaRepository.findById(id).get();

            Optional<Region> regionId = regionRepository.findById(dto.getRegion_id());
            if (!regionId.isPresent()) {
                return GlobalResponse.builder().message("Data region not found").status(HttpStatus.BAD_REQUEST).build();
            }

            Area area = new Area(
                    id,
                    dto.getName(),
                    areaGet.getCreated_at(),
                    new Date(),
                    0,
                    regionId.get());

            Area response = areaRepository.save(area);
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

            Area areaGet = areaRepository.findById(id).get();

            GlobalResponse branch = branchService.findSpecificByAreaId(id);
            if (branch.getData() != null) {
                return GlobalResponse
                        .builder()
                        .message("Cannot delete because relation")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Area area = areaGet;
            area.setIs_delete(1);
            area.setUpdated_at(new Date());

            Area response = areaRepository.save(area);
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
