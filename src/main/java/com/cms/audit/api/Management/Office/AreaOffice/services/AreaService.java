package com.cms.audit.api.Management.Office.AreaOffice.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
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
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.Management.Office.AreaOffice.dto.AreaDTO;
import com.cms.audit.api.Management.Office.AreaOffice.dto.response.AreaInterface;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.AreaOffice.repository.PagArea;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private PagArea pagArea;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private RegionRepository regionRepository;

    public GlobalResponse findAll(String name, int page, int size, Long regionId, String regionName) {
        try {
            Specification<Area> spec = Specification
                    .where(new SpecificationFIlter<Area>().byNameLike(name))
                    .and(new SpecificationFIlter<Area>().regionIdEqual(regionId))
                    .and(new SpecificationFIlter<Area>().regionNameLike(regionName))
                    .and(new SpecificationFIlter<Area>().isNotDeleted())
                    .and(new SpecificationFIlter<Area>().orderByIdAsc());
            Page<Area> response = pagArea.findAll(spec, PageRequest.of(page, size));
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

            List<AreaInterface> response = new ArrayList<>();
            if (getUser.getLevel().getCode().equals("B")) {
                for (int i = 0; i < getUser.getRegionId().size(); i++) {
                    List<AreaInterface> getBranch = areaRepository
                            .findSpecificAreaByRegionId(getUser.getRegionId().get(i));
                    for (int u = 0; u < getBranch.size(); u++) {
                        response.add(getBranch.get(u));
                    }
                }
            } else if (getUser.getLevel().getCode().equals("A") || getUser.getLevel().getCode().equals("A")) {
                response = areaRepository.findSpecificArea();
            } else {
                return GlobalResponse.builder().message("Selain audit Area, Pusat dan Leader tidak bisa mengakses ini")
                        .status(HttpStatus.UNAUTHORIZED).build();
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

    public GlobalResponse findOne(Long id) {
        try {
            Optional<Area> response = areaRepository.findOneAreaById(id);
            if (!response.isPresent()) {
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

    public GlobalResponse findOneByRegionId(Long id, int page, int size) {
        try {
            Optional<Region> setRegion = regionRepository.findById(id);
            if (!setRegion.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Region with id:" + setRegion.get().getId() + " is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            Page<Area> response = pagArea.findByRegion(setRegion.get(), PageRequest.of(page, size));
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
            List<AreaInterface> response = areaRepository.findSpecificAreaByRegionId(id);
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

    public GlobalResponse save(AreaDTO dto) {
        try {

            if (dto.getName() == null || dto.getName() == "") {
                return GlobalResponse.builder().message("Data tidak boleh kosong")
                        .errorMessage("Data tidak boleh kosong").status(HttpStatus.BAD_REQUEST).build();
            }

            if (dto.getRegion_id() == null) {
                return GlobalResponse.builder().message("Region tidak boleh kosong")
                        .errorMessage("Region tidak boleh kosong").status(HttpStatus.BAD_REQUEST).build();
            }

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

            Area areaGet = areaRepository.findById(id).get();

            List<Branch> branch = branchRepository.findBranchByAreaId(id);
            if (!branch.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Tidak bisa menghapus karena relasi tabel")
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
