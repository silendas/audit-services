package com.cms.audit.api.Management.Office.BranchOffice.services;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.BranchOffice.dto.BranchDTO;
import com.cms.audit.api.Management.Office.BranchOffice.dto.response.BranchInterface;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Office.BranchOffice.repository.PagBranch;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private PagBranch pagBranch;

    public GlobalResponse findAll(String name, int page, int size, Long areaId) {
        try {
            Page<Branch> response;
            if (name != null) {
                response = pagBranch.findByNameContaining(name, PageRequest.of(page, size));
            } else if (areaId != null) {
                response = pagBranch.findBranchByAreaId(areaId, PageRequest.of(page, size));
            } else {
                response = pagBranch.findAllBranch(PageRequest.of(page, size));
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
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<BranchInterface> response = new ArrayList<>();
            if(getUser.getLevel().getId() == 2){
                for(int i = 0; i<getUser.getRegionId().size(); i++){
                    List<BranchInterface> getBranch = branchRepository.findSpecificBranchByRegionId(getUser.getRegionId().get(i));
                    for(int u = 0; u<getBranch.size();u++){
                    response.add(getBranch.get(u));
                    }
                }
            }else if(getUser.getLevel().getId() == 3){
                for(int i = 0; i<getUser.getBranchId().size(); i++){
                    Optional<BranchInterface> getBranch = branchRepository.findSpecificBranchById(getUser.getBranchId().get(i));
                    response.add(getBranch.get());
                }
            } else if(getUser.getLevel().getId() == 1){
                response = branchRepository.findSpecificBranch();
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

    public GlobalResponse findOne(Long id) {
        try {
            Optional<Branch> response = branchRepository.findById(id);
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

    public GlobalResponse findByAreaId(Long id, int page, int size) {
        try {
            Optional<Area> setArea = areaRepository.findById(id);
            if (!setArea.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Area with id:" + setArea.get().getId() + "is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Page<Branch> response = pagBranch.findByArea(setArea.get(), PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(response)
                        .build();
            }
            // Map<String, Object> data = new LinkedHashMap<>();
            // data.put("recordCount", response.size());
            // data.put("result", response);
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

    public GlobalResponse findSpecificByAreaId(Long id) {
        try {
            List<BranchInterface> response = branchRepository.findSpecificBranchByAreaId(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(null)
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

    public GlobalResponse findSpecificByName(String name) {
        try {
            List<Branch> getBranch = branchRepository.findByNameContainingIgnoreCase(name);
            List<Object> response = new ArrayList<>();
            for(int i=0;i<getBranch.size();i++){
                Map<String,Object> mapping = new LinkedHashMap<>();
                mapping.put("id",getBranch.get(i).getId());
                mapping.put("name",getBranch.get(i).getName());
                response.add(mapping);
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .data(null)
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
            List<BranchInterface> response = branchRepository.findSpecificBranchByRegionId(id);
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

    public GlobalResponse save(BranchDTO branchDTO) {
        try {

            Optional<Area> areaId = areaRepository.findById(branchDTO.getArea_id());
            if (!areaId.isPresent()) {
                return GlobalResponse.builder().message("Data area not found").status(HttpStatus.BAD_REQUEST).build();
            }

            Branch branch = new Branch(
                    null,
                    branchDTO.getName(),
                    new Date(),
                    new Date(),
                    0,
                    areaId.get());

            Branch response = branchRepository.save(branch);
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

    public GlobalResponse edit(BranchDTO branchDTO, Long id) {
        try {

            Branch branchGet = branchRepository.findById(id).get();

            Optional<Area> areaId = areaRepository.findById(branchDTO.getArea_id());
            if (!areaId.isPresent()) {
                return GlobalResponse.builder().message("Data area not found").status(HttpStatus.BAD_REQUEST).build();
            }

            Branch branch = new Branch(
                    id,
                    branchDTO.getName(),
                    branchGet.getCreated_at(),
                    new Date(),
                    0,
                    areaId.get());

            Branch response = branchRepository.save(branch);
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
            Branch branchGet = branchRepository.findById(id).get();

            Area areaId = Area
                    .builder()
                    .id(branchGet.getArea().getId())
                    .build();

            Branch branch = new Branch(
                    id,
                    branchGet.getName(),
                    branchGet.getCreated_at(),
                    new Date(),
                    1,
                    areaId);

            Branch response = branchRepository.save(branch);
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
