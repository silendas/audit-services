package com.cms.audit.api.Management.Office.RegionOffice.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
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
import com.cms.audit.api.Management.Office.AreaOffice.services.AreaService;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.MainOffice.repository.MainRepository;
import com.cms.audit.api.Management.Office.RegionOffice.dto.RegionDTO;
import com.cms.audit.api.Management.Office.RegionOffice.dto.response.RegionInterface;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.PagRegion;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MainRepository mainRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private PagRegion pagRegion;

    public GlobalResponse findAll(String name, int page, int size, Long mainId) {
        try {
            Page<Region> response;
            if (name != null) {
                response = pagRegion.findByNameContaining(name, PageRequest.of(page, size));
            } else if (mainId != null) {
                response = pagRegion.findRegionByMainId(mainId, PageRequest.of(page, size));
            } else {
                response = pagRegion.findAllRegion(PageRequest.of(page, size));
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

            List<RegionInterface> response = regionRepository.findSpecificRegion();
            if (getUser.getLevel().getCode().equals("B")) {
                for (int i = 0; i < getUser.getRegionId().size(); i++) {
                    List<RegionInterface> getBranch = regionRepository
                            .findSpecificRegionById(getUser.getRegionId().get(i));
                    for (int u = 0; u < getBranch.size(); u++) {
                        response.add(getBranch.get(u));
                    }
                }
            } else if (getUser.getLevel().getCode().equals("A") || getUser.getLevel().getCode().equals("A")) {
                response = regionRepository.findSpecificRegion();
            } else {
                return GlobalResponse
                        .builder()
                        .message("User selain Area, Pusat dan Leader tidak bisa akses")
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
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

    public List<Region> findByUser(Long userId) {
        List<Region> listRegion = new ArrayList<>();
        Optional<User> getUser = userRepository.findById(userId);
        if (!getUser.get().getRegionId().isEmpty()) {
            for (int i = 0; i < getUser.get().getRegionId().size(); i++) {
                Optional<Region> getregion = regionRepository.findById(userId);
                if (!getregion.isPresent()) {
                    continue;
                }
                listRegion.add(getregion.get());
            }
            return listRegion;
        } else {
            return listRegion;
        }
    }

    public GlobalResponse findOne(Long id) {
        try {
            Optional<Region> response = regionRepository.findOneRegionById(id);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
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

    public GlobalResponse findSpecificByMainId(Long id) {
        try {
            List<RegionInterface> response = regionRepository.findSpecificRegionByMainId(id);
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

    public GlobalResponse save(RegionDTO dto) {
        try {

            Optional<Main> mainId = mainRepository.findById(dto.getMain_id());
            if (!mainId.isPresent()) {
                return GlobalResponse.builder().message("Data main not found").status(HttpStatus.BAD_REQUEST).build();
            }

            Region region = new Region(
                    null,
                    dto.getName(),
                    new Date(),
                    new Date(),
                    0,
                    mainId.get());

            Region response = regionRepository.save(region);
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

    public GlobalResponse edit(RegionDTO dto, Long id) {
        try {

            Region regionGet = regionRepository.findById(id).get();

            Optional<Main> mainId = mainRepository.findById(dto.getMain_id());
            if (!mainId.isPresent()) {
                return GlobalResponse.builder().message("Data main not found").status(HttpStatus.BAD_REQUEST).build();
            }

            Region region = new Region(
                    id,
                    dto.getName(),
                    regionGet.getCreated_at(),
                    new Date(),
                    0,
                    mainId.get());

            Region response = regionRepository.save(region);
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
            Region regionGet = regionRepository.findById(id).get();

            List<Area> area = areaRepository.findAreaByRegionId(id);
            if (!area.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Tidak bisa menghapus karena relasi tabel")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Region region = regionGet;
            region.setIs_delete(1);
            region.setUpdated_at(new Date());

            Region response = regionRepository.save(region);
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
