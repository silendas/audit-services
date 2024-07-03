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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.BranchOffice.dto.BranchDTO;
import com.cms.audit.api.Management.Office.BranchOffice.dto.response.BranchInterface;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Office.BranchOffice.repository.PagBranch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PagBranch pagBranch;

    public GlobalResponse findAll(String name, int page, int size, Long areaId, String areaName, String regionName) {
        try {
            Specification<Branch> spec = Specification
                    .where(Specification.where(new SpecificationFIlter<Branch>().byNameLike(name))
                            .or(new SpecificationFIlter<Branch>().branchToRegionNameLike(regionName)
                                    .or(new SpecificationFIlter<Branch>().areaNameLike(areaName))))
                    .and(new SpecificationFIlter<Branch>().areaIdEqual(areaId))
                    .and(new SpecificationFIlter<Branch>().isNotDeleted())
                    .and(new SpecificationFIlter<Branch>().orderByIdAsc());

            Page<Branch> response = pagBranch.findAll(spec, PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
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
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<BranchInterface> response = new ArrayList<>();
            // if (getUser.getLevel().getCode().equals("B")) {
            // for (int i = 0; i < getUser.getRegionId().size(); i++) {
            // List<BranchInterface> getBranch = branchRepository
            // .findSpecificBranchByRegionId(getUser.getRegionId().get(i));
            // for (int u = 0; u < getBranch.size(); u++) {
            // response.add(getBranch.get(u));
            // }
            // }
            // } else if (getUser.getLevel().getCode().equals("C")) {
            // for (int i = 0; i < getUser.getBranchId().size(); i++) {
            // Optional<BranchInterface> getBranch = branchRepository
            // .findSpecificBranchById(getUser.getBranchId().get(i));
            // response.add(getBranch.get());
            // }
            // } else if (getUser.getLevel().getCode().equals("A") ||
            // getUser.getLevel().getCode().equals("A")) {
            // response = branchRepository.findSpecificBranch();
            // }
            response = branchRepository.findSpecificBranch();
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
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
            Optional<Branch> response = branchRepository.findById(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.BAD_REQUEST)
                        .data(response)
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
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
                        .build();
            }
            // Map<String, Object> data = new LinkedHashMap<>();
            // data.put("recordCount", response.size());
            // data.put("result", response);
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

    public GlobalResponse findSpecificByAreaId(List<Long> id) {
        try {
            List<BranchInterface> response = new ArrayList<>();
            if (id.isEmpty()) {
                return GlobalResponse.builder().message("Area id tidak boleh kosong")
                        .errorMessage("Area tidak diisi").status(HttpStatus.BAD_REQUEST)
                        .data(response)
                        .build();
            }
            for (int i = 0; i < id.size(); i++) {

                Optional<Area> getArea = areaRepository.findById(id.get(i));
                if (!getArea.isPresent()) {
                    return GlobalResponse.builder().message("Area tidak ditemukan")
                            .errorMessage("Area with id:" + id.get(i) + " is not found").status(HttpStatus.BAD_REQUEST)
                            .data(response)
                            .build();
                }

                List<BranchInterface> getBranch = branchRepository.findSpecificBranchByAreaId(id.get(i));
                if (!getBranch.isEmpty()) {
                    for (int u = 0; u < getBranch.size(); u++) {
                        response.add(getBranch.get(u));
                    }
                }

            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public GlobalResponse findSpecificByName(String name) {
        try {
            List<Branch> getBranch = branchRepository.findByNameContainingIgnoreCase(name);
            List<Object> response = new ArrayList<>();
            for (int i = 0; i < getBranch.size(); i++) {
                Map<String, Object> mapping = new LinkedHashMap<>();
                mapping.put("id", getBranch.get(i).getId());
                mapping.put("name", getBranch.get(i).getName());
                response.add(mapping);
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public List<Object> findByUser(Long userId) {
        Optional<User> getUser = userRepository.findById(userId);
        if (!getUser.isPresent()) {
            return null;
        }
        List<Branch> getBranch = new ArrayList<>();
        if (!getUser.get().getBranchId().isEmpty()) {
            for (int i = 0; i < getUser.get().getBranchId().size(); i++) {
                Optional<Branch> getBranchAgain = branchRepository.findById(getUser.get().getBranchId().get(i));
                if (getBranchAgain.isPresent()) {
                    getBranch.add(getBranchAgain.get());
                }
            }
        } else {
            if (!getUser.get().getRegionId().isEmpty()) {
                for (int i = 0; i < getUser.get().getRegionId().size(); i++) {
                    List<Branch> getBranchAgain = branchRepository
                            .findBranchByRegionId(getUser.get().getRegionId().get(i));
                    if (!getBranchAgain.isEmpty()) {
                        for (int u = 0; u < getBranchAgain.size(); u++) {
                            if (!getBranch.contains(getBranchAgain.get(u))) {
                                getBranch.add(getBranchAgain.get(u));
                            }
                        }
                    }
                }
            } else {
                return null;
            }
        }
        List<Object> response = new ArrayList<>();
        for (int i = 0; i < getBranch.size(); i++) {
            Map<String, Object> mapping = new LinkedHashMap<>();
            mapping.put("id", getBranch.get(i).getId());
            mapping.put("name", getBranch.get(i).getName());
            response.add(mapping);
        }
        if (response.isEmpty()) {
            return null;
        }
        return response;
    }

    public GlobalResponse findSpecificByUserid(Long userId) {
        try {
            Optional<User> getUser = userRepository.findById(userId);
            if (!getUser.isPresent()) {
                return GlobalResponse.builder().message("User tidak ditemukan")
                        .errorMessage("User dengan id :" + userId + " tidak ditemukan")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            List<Branch> getBranch = new ArrayList<>();
            if (!getUser.get().getBranchId().isEmpty()) {
                for (int i = 0; i < getUser.get().getBranchId().size(); i++) {
                    Optional<Branch> getBranchAgain = branchRepository.findById(getUser.get().getBranchId().get(i));
                    if (getBranchAgain.isPresent()) {
                        getBranch.add(getBranchAgain.get());
                    }
                }
            } else {
                if (!getUser.get().getRegionId().isEmpty()) {
                    for (int i = 0; i < getUser.get().getRegionId().size(); i++) {
                        List<Branch> getBranchAgain = branchRepository
                                .findBranchByRegionId(getUser.get().getRegionId().get(i));
                        if (!getBranchAgain.isEmpty()) {
                            for (int u = 0; u < getBranchAgain.size(); u++) {
                                if (!getBranch.contains(getBranchAgain.get(u))) {
                                    getBranch.add(getBranchAgain.get(u));
                                }
                            }
                        }
                    }
                } else {
                    return GlobalResponse
                            .builder()
                            .message("Berhasil menampilkan data")
                            .data(branchRepository.findSpecificBranch())
                            .status(HttpStatus.OK)
                            .build();
                }
            }
            List<Object> response = new ArrayList<>();
            for (int i = 0; i < getBranch.size(); i++) {
                Map<String, Object> mapping = new LinkedHashMap<>();
                mapping.put("id", getBranch.get(i).getId());
                mapping.put("name", getBranch.get(i).getName());
                response.add(mapping);
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public GlobalResponse findSpecificByRegionId(Long id) {
        try {
            List<BranchInterface> response = branchRepository.findSpecificBranchByRegionId(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK)
                        .data(response)
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

    public GlobalResponse save(BranchDTO branchDTO) {
        try {

            if (branchDTO.getArea_id() == null) {
                return GlobalResponse.builder().message("Area tidak boleh kosong").errorMessage("Area tidak diisi")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            if (branchDTO.getName() == null || branchDTO.getName() == "") {
                return GlobalResponse.builder().message("Name tidak boleh kosong").errorMessage("Name tidak diisi")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
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
