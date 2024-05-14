package com.cms.audit.api.Management.User.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.dto.response.LogUserResponse;
import com.cms.audit.api.Management.User.models.LogUser;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.LogUserRepository;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LogUserService {

   
    @Autowired
    private LogUserRepository repository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    public LogUserResponse objUserDetail(LogUser response) {
        LogUserResponse user = new LogUserResponse();
        user.setId(response.getId());
        user.setLevel(response.getLevel());
        user.setMain(response.getMain());
        user.setRegion(fetchRegions(response.getRegionId()));
        user.setArea(fetchAreas(response.getAreaId()));
        user.setBranch(fetchBranches(response.getBranchId()));
        user.setEmail(response.getEmail());
        user.setUsername(response.getUsername());
        user.setFullname(response.getFullname());
        user.setInitial_name(response.getInitial_name());
        user.setNip(response.getNip());
        user.setIs_active(response.getIs_active());
        user.setCreated_at(response.getCreated_at());
        user.setCreated_by(fetchUser(response.getCreated_by()));
        return user;
    }

    private List<Region> fetchRegions(List<Long> regionIds) {
        List<Region> regions = new ArrayList<>();
        if (regionIds != null && !regionIds.isEmpty()) {
            regions = regionRepository.findAllById(regionIds);
        }
        return regions;
    }

    private List<Area> fetchAreas(List<Long> areaIds) {
        List<Area> areas = new ArrayList<>();
        if (areaIds != null && !areaIds.isEmpty()) {
            areas = areaRepository.findAllById(areaIds);
        }
        return areas;
    }

    private List<Branch> fetchBranches(List<Long> branchIds) {
        List<Branch> branches = new ArrayList<>();
        if (branchIds != null && !branchIds.isEmpty()) {
            branches = branchRepository.findAllById(branchIds);
        }
        return branches;
    }

    private Map<String, Object> fetchUser(Long userId) {
        Map<String, Object> objUser = new LinkedHashMap<>();
        Optional<User> createdBy = userRepository.findById(userId);
        if (createdBy.isPresent()) {
            objUser.put("id", createdBy.get().getId());
            objUser.put("fullname", createdBy.get().getFullname());
            objUser.put("initial_name", createdBy.get().getInitial_name());
            objUser.put("nip", createdBy.get().getNip());
            objUser.put("level", createdBy.get().getLevel());
        }
        return objUser;
    }

    public List<LogUserResponse> pageUserDetail(List<LogUser> response) {
        List<LogUserResponse> listUser = new ArrayList<>();
        for (LogUser user : response) {
            LogUserResponse userResponse = objUserDetail(user);
            listUser.add(userResponse);
        }
        return listUser;
    }

    public ResponseEntity<Object> getLogByUserId(Long id) {
        try {
            List<LogUser> getLog = new ArrayList<>();
            if (id != null) {
                getLog = repository.findByUserId(id);
            } else {
                getLog = repository.findAll();
            }
            GlobalResponse response = new GlobalResponse();
            if (getLog.isEmpty()) {
                response = GlobalResponse.builder().data(getLog)
                        .message("Data tidak ditemukan")
                        .status(HttpStatus.OK).build();
            } else {
                response = GlobalResponse.builder().data(pageUserDetail(getLog))
                        .message("Data berhasil ditampilkan")
                        .status(HttpStatus.OK).build();
            }

            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    null);
        } catch (Exception e) {
            return ResponseEntittyHandler.errorResponse("Err: " + e,"Terjadi kesalahan",
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<Object> getLogById(Long id) {
        try {
            Optional<LogUser> getLog = repository.findById(id);
            if (!getLog.isPresent()) {
                return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan",
                        HttpStatus.BAD_REQUEST);
            }
            GlobalResponse response = GlobalResponse.builder().data(objUserDetail(getLog.get()))
                    .message("Data berhasil ditampilkan")
                    .status(HttpStatus.OK).build();
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    null);
        } catch (Exception e) {
            return ResponseEntittyHandler.errorResponse("Terjadi kesalahan", "Err: " + e,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public void insertAuto(User dto) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LogUser setLog = new LogUser();
        setLog.setUser(dto);
        setLog.setAreaId(dto.getAreaId());
        setLog.setBranchId(dto.getBranchId());
        setLog.setRegionId(dto.getRegionId());
        setLog.setMain(dto.getMain());
        setLog.setEmail(dto.getEmail());
        setLog.setFullname(dto.getFullname());
        setLog.setInitial_name(dto.getInitial_name());
        setLog.setIs_active(dto.getIs_active());
        setLog.setIs_delete(dto.getIs_delete());
        setLog.setLevel(dto.getLevel());
        setLog.setNip(dto.getNip());
        setLog.setPassword(dto.getPassword());
        setLog.setRole(dto.getRole());
        setLog.setUsername(dto.getUsername());
        setLog.setCreated_at(new Date());
        setLog.setCreated_by(getUser.getId());

        repository.save(setLog);

    }

}
