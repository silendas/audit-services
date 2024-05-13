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
import com.cms.audit.api.Management.User.models.EStatusLog;
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

        List<Region> region = new ArrayList<>();
        if (!response.getRegionId().isEmpty()) {
            for (int i = 0; i < response.getRegionId().size(); i++) {
                region.add(regionRepository.findById(response.getRegionId().get(i))
                        .orElse(null));
            }
        }
        Optional<User> createdBy = userRepository.findById(response.getId());
        List<Area> area = new ArrayList<>();
        if (!response.getAreaId().isEmpty()) {
            for (int i = 0; i < response.getAreaId().size(); i++) {
                area.add(areaRepository.findById(response.getAreaId().get(i))
                        .orElse(null));
            }
        }
        List<Branch> branch = new ArrayList<>();
        if (!response.getBranchId().isEmpty()) {
            for (int i = 0; i < response.getBranchId().size(); i++) {
                branch.add(branchRepository.findById(response.getBranchId().get(i))
                        .orElse(null));
            }
        }

        LogUserResponse user = new LogUserResponse();
        user.setId(response.getId());
        user.setLevel(response.getLevel());
        user.setMain(response.getMain());
        user.setRegion(region);
        user.setArea(area);
        user.setBranch(branch);
        user.setEmail(response.getEmail());
        user.setUsername(response.getUsername());
        user.setFullname(response.getFullname());
        user.setAction(response.getAction());
        user.setInitial_name(response.getInitial_name());
        user.setNip(response.getNip());
        user.setIs_active(response.getIs_active());
        user.setCreated_at(response.getCreated_at());

        Map<String, Object> objUser = new LinkedHashMap<>();
        objUser.put("id", createdBy.get().getId());
        objUser.put("fullname", createdBy.get().getFullname());
        objUser.put("initial_name", createdBy.get().getInitial_name());
        objUser.put("nip", createdBy.get().getNip());
        objUser.put("level", createdBy.get().getLevel());

        user.setCreated_by(objUser);

        return user;

    }

    public List<LogUserResponse> pageUserDetail(List<LogUser> response) {

        List<LogUserResponse> listUser = new ArrayList<>();
        for (int u = 0; u < response.size(); u++) {
            List<Region> region = new ArrayList<>();
            if (!response.get(u).getRegionId().isEmpty()) {
                for (int i = 0; i < response.get(u).getRegionId().size(); i++) {
                    region.add(regionRepository.findById(response.get(u).getRegionId().get(i))
                            .orElse(null));
                }
            }

            List<Area> area = new ArrayList<>();
            if (!response.get(u).getAreaId().isEmpty()) {
                for (int i = 0; i < response.get(u).getAreaId().size(); i++) {
                    area.add(areaRepository.findById(response.get(u).getAreaId().get(i))
                            .orElse(null));
                }
            }
            List<Branch> branch = new ArrayList<>();
            if (!response.get(u).getBranchId().isEmpty()) {
                for (int i = 0; i < response.get(u).getBranchId().size(); i++) {
                    branch.add(branchRepository.findById(response.get(u).getBranchId().get(i))
                            .orElse(null));
                }
            }

            LogUserResponse user = new LogUserResponse();
            user.setId(response.get(u).getId());
            user.setLevel(response.get(u).getLevel());
            user.setMain(response.get(u).getMain());
            user.setRegion(region);
            user.setArea(area);
            user.setBranch(branch);
            user.setEmail(response.get(u).getEmail());
            user.setAction(response.get(u).getAction());
            user.setUsername(response.get(u).getUsername());
            user.setFullname(response.get(u).getFullname());
            user.setInitial_name(response.get(u).getInitial_name());
            user.setNip(response.get(u).getNip());
            user.setIs_active(response.get(u).getIs_active());
            user.setCreated_at(response.get(u).getCreated_at());

            Optional<User> createdBy = userRepository.findById(response.get(u).getId());

            Map<String, Object> objUser = new LinkedHashMap<>();
            objUser.put("id", createdBy.get().getId());
            objUser.put("fullname", createdBy.get().getFullname());
            objUser.put("initial_name", createdBy.get().getInitial_name());
            objUser.put("nip", createdBy.get().getNip());
            objUser.put("level", createdBy.get().getLevel());

            user.setCreated_by(objUser);

            listUser.add(user);

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

    public void insertAuto(User dto, EStatusLog status) {

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
        setLog.setAction(status);
        setLog.setNip(dto.getNip());
        setLog.setPassword(dto.getPassword());
        setLog.setRole(dto.getRole());
        setLog.setUsername(dto.getUsername());
        setLog.setCreated_at(new Date());
        setLog.setCreated_by(getUser.getId());
        repository.save(setLog);

    }

}
