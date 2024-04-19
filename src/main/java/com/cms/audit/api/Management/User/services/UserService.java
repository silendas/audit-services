package com.cms.audit.api.Management.User.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.AreaOffice.repository.AreaRepository;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.MainOffice.repository.MainRepository;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.User.dto.ChangePasswordDTO;
import com.cms.audit.api.Management.User.dto.ChangeProfileDTO;
import com.cms.audit.api.Management.User.dto.DropDownUserDTO;
import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.dto.response.DropDownUser;
import com.cms.audit.api.Management.User.dto.response.UserResponse;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.PagUser;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class UserService {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private MainRepository mainRepository;

        @Autowired
        private RegionRepository regionRepository;

        @Autowired
        private AreaRepository areaRepository;

        @Autowired
        private BranchRepository branchRepository;

        @Autowired
        private PagUser pagUser;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public GlobalResponse findAll(int page, int size, String username) {
                User getUser = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "user with username " + username + " is undefined"));
                Pageable pageable = PageRequest.of(page, size);
                List<User> user = new ArrayList<>();
                if (getUser.getLevel().getId() == 1) {
                        user = userRepository.findAll();
                } else if (getUser.getLevel().getId() == 2) {
                        if (getUser.getRegionId() != null) {
                                List<User> userAgain = userRepository.findAll();
                                for (int u = 0; u < userAgain.size(); u++) {
                                        for (int i = 0; i < getUser.getRegionId().size(); i++) {
                                                Long regionId = getUser.getRegionId().get(i);
                                                if (userAgain.get(u).getRegionId().size() == 0
                                                                || userAgain.get(u).getRegionId() == null) {
                                                        if (userAgain.get(u).getBranchId() != null
                                                                        || userAgain.get(u).getBranchId().size() != 0) {
                                                                for (int e = 0; e < userAgain.get(u).getBranchId()
                                                                                .size(); e++) {
                                                                        Optional<Branch> branchAgain = branchRepository
                                                                                        .findById(userAgain.get(u)
                                                                                                        .getBranchId()
                                                                                                        .get(e));
                                                                        if (branchAgain.get().getArea().getRegion()
                                                                                        .getId() == regionId) {
                                                                                user.add(userAgain.get(u));
                                                                        }

                                                                }
                                                        }
                                                } else {
                                                        for (int o = 0; o < userAgain.get(u).getRegionId()
                                                                        .size(); o++) {
                                                                if (regionId == userAgain.get(u).getRegionId().get(o)) {
                                                                        user.add(userAgain.get(u));
                                                                }
                                                        }
                                                }
                                        }
                                }
                        } else {
                                List<User> userAgain = userRepository.findAll();
                                Long lastId = null;
                                for (int i = 0; i < getUser.getBranchId().size(); i++) {
                                        Branch getBranch = branchRepository
                                                        .findById(getUser.getBranchId().get(i)).orElseThrow();
                                        Long regionId = getBranch.getArea().getRegion().getId();
                                        if (lastId != regionId) {
                                                for (int u = 0; u < userAgain.size(); u++) {
                                                        if (userAgain.get(u).getRegionId() == null) {
                                                                if (userAgain.get(u).getBranchId() != null) {
                                                                        for (int e = 0; e < userAgain.get(u)
                                                                                        .getBranchId().size(); e++) {
                                                                                Branch branchAgain = branchRepository
                                                                                                .findById(userAgain
                                                                                                                .get(u)
                                                                                                                .getBranchId()
                                                                                                                .get(e))
                                                                                                .orElseThrow();
                                                                                if (regionId == branchAgain.getArea()
                                                                                                .getRegion().getId()) {

                                                                                        user.add(userAgain.get(u));
                                                                                        break;
                                                                                }
                                                                        }
                                                                }
                                                        } else {
                                                                for (int o = 0; o < userAgain.get(u).getRegionId()
                                                                                .size(); o++) {
                                                                        if (regionId == userAgain.get(u).getRegionId()
                                                                                        .get(o)) {
                                                                                user.add(userAgain.get(u));
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                        lastId = regionId;
                                }
                        }
                }
                try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()), user.size());
                        List<User> pageContent = user.subList(start, end);
                        Page<User> response = new PageImpl<>(pageContent, pageable, user.size());
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Data not found")
                                        .status(HttpStatus.OK)
                                        .build();
                }
        }

        public GlobalResponse findOne(Long id) {
                try {
                        Optional<User> response = userRepository.findById(id);
                        if (!response.isPresent()) {
                                return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK)
                                                .build();
                        }
                        List<Region> region = new ArrayList<>();
                        if (!response.get().getRegionId().isEmpty()) {
                                for (int i = 0; i < response.get().getRegionId().size(); i++) {
                                        region.add(regionRepository.findById(response.get().getRegionId().get(i))
                                                        .orElse(null));
                                }
                        }
                        List<Area> area = new ArrayList<>();
                        if (!response.get().getAreaId().isEmpty()) {
                                for (int i = 0; i < response.get().getAreaId().size(); i++) {
                                        area.add(areaRepository.findById(response.get().getAreaId().get(i))
                                                        .orElse(null));
                                }
                        }
                        List<Branch> branch = new ArrayList<>();
                        if (!response.get().getBranchId().isEmpty()) {
                                for (int i = 0; i < response.get().getBranchId().size(); i++) {
                                        branch.add(branchRepository.findById(response.get().getBranchId().get(i))
                                                        .orElse(null));
                                }
                        }

                        UserResponse user = new UserResponse();
                        user.setId(response.get().getId());
                        user.setLevel(response.get().getLevel());
                        user.setMain(response.get().getMain());
                        user.setRegion(region);
                        user.setArea(area);
                        user.setBranch(branch);
                        user.setEmail(response.get().getEmail());
                        user.setUsername(response.get().getUsername());
                        user.setFullname(response.get().getFullname());
                        user.setInitial_name(response.get().getInitial_name());
                        user.setNip(response.get().getNip());
                        user.setIs_active(response.get().getIs_active());
                        user.setCreated_at(response.get().getCreated_at());
                        user.setUpdated_at(response.get().getUpdated_at());

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(user)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }

        }

        public GlobalResponse findOneByUsername(String username) {
                try {
                        Optional<User> response = userRepository.findByUsername(username);
                        if (!response.isPresent()) {
                                return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK)
                                                .build();
                        }
                        List<Region> region = new ArrayList<>();
                        if (!response.get().getRegionId().isEmpty()) {
                                for (int i = 0; i < response.get().getRegionId().size(); i++) {
                                        region.add(regionRepository.findById(response.get().getRegionId().get(i))
                                                        .orElse(null));
                                }
                        } else {
                                region = null;
                        }
                        List<Area> area = new ArrayList<>();
                        if (!response.get().getAreaId().isEmpty()) {
                                for (int i = 0; i < response.get().getAreaId().size(); i++) {
                                        area.add(areaRepository.findById(response.get().getAreaId().get(i))
                                                        .orElse(null));
                                }
                        } else {
                                area = null;
                        }
                        List<Branch> branch = new ArrayList<>();
                        if (!response.get().getBranchId().isEmpty()) {
                                for (int i = 0; i < response.get().getBranchId().size(); i++) {
                                        branch.add(branchRepository.findById(response.get().getBranchId().get(i))
                                                        .orElse(null));
                                }
                        } else {
                                branch = null;
                        }

                        UserResponse user = new UserResponse();
                        user.setId(response.get().getId());
                        user.setLevel(response.get().getLevel());
                        user.setMain(response.get().getMain());
                        user.setRegion(region);
                        user.setArea(area);
                        user.setBranch(branch);
                        user.setEmail(response.get().getEmail());
                        user.setUsername(response.get().getUsername());
                        user.setFullname(response.get().getFullname());
                        user.setInitial_name(response.get().getInitial_name());
                        user.setNip(response.get().getNip());
                        user.setIs_active(response.get().getIs_active());
                        user.setCreated_at(response.get().getCreated_at());
                        user.setUpdated_at(response.get().getUpdated_at());
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(user)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }

        }

        public GlobalResponse findOneByMainId(Long id, int page, int size) {
                try {
                        Optional<Main> setMain = mainRepository.findById(id);
                        if (!setMain.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data main not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        Page<User> response = pagUser.findByMain(setMain.get(), PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        // public GlobalResponse findOneByRegionId(Long id, int page, int size) {
        // try {
        // Optional<Region> set = regionRepository.findById(id);
        // if (!set.isPresent()) {
        // return GlobalResponse
        // .builder()
        // .message("Data not found")
        // .status(HttpStatus.OK)
        // .build();
        // }
        // Page<User> response = pagUser.findByRegion(set.get(), PageRequest.of(page,
        // size));
        // if (response.isEmpty()) {
        // return GlobalResponse.builder().message("Data not
        // found").status(HttpStatus.OK)
        // .build();
        // }
        // return GlobalResponse
        // .builder()
        // .message("Success")
        // .data(response)
        // .status(HttpStatus.OK)
        // .build();
        // } catch (DataException e) {
        // throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data
        // error");

        // } catch (Exception e) {
        // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server
        // error");
        // }

        // }

        public GlobalResponse dropDown() {
                try {
                        List<DropDownUser> response = userRepository.findDropDown();
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("Data not found").data(response).status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public List<DropDownUser> dropDownByManyRegion(Long id) {
                try {
                        List<DropDownUser> response = userRepository.findDropDownByRegion(id);
                        if (response.isEmpty()) {
                                return null;
                        }
                        return response;
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public GlobalResponse dropDownByRegionId(Long id) {
                try {
                        List<User> getUser = userRepository.findAll();
                        if (getUser.isEmpty()) {
                                return null;
                        }
                        List<DropDownUserDTO> response = new ArrayList<>();
                        for (int i = 0; i < getUser.size(); i++) {
                                User userAgain = getUser.get(i);
                                for (int r = 0; r < userAgain.getRegionId().size(); r++) {
                                        if (userAgain.getRegionId().get(r).equals(id)) {
                                                DropDownUserDTO setUser = new DropDownUserDTO();
                                                setUser.setFullname(userAgain.getFullname());
                                                setUser.setId(userAgain.getId());
                                                setUser.setInitial_name(userAgain.getInitial_name());
                                                response.add(setUser);
                                        }
                                }
                                for (int b = 0; b < userAgain.getBranchId().size(); b++) {
                                        Optional<Branch> getBranch = branchRepository
                                                        .findById(userAgain.getBranchId().get(i));
                                        if (getBranch.get().getArea().getRegion().getId().equals(id)) {
                                                if (!response.contains(userAgain)) {
                                                        DropDownUserDTO setUser = new DropDownUserDTO();
                                                        setUser.setFullname(userAgain.getFullname());
                                                        setUser.setId(userAgain.getId());
                                                        setUser.setInitial_name(userAgain.getInitial_name());
                                                        response.add(setUser);
                                                }
                                        }
                                }
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("Data not found").data(response).status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public GlobalResponse dropDownByMainId(Long id) {
                try {
                        List<DropDownUser> response = userRepository.findDropDownByMain(id);
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        // public GlobalResponse findOneByAreaId(Long id, int page, int size) {
        // try {
        // Area set = areaRepository.findById(id)
        // .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK));
        // Page<User> response = pagUser.findByArea(set, PageRequest.of(page, size));
        // if (response.isEmpty()) {
        // return GlobalResponse.builder().message("Data not
        // found").status(HttpStatus.OK)
        // .build();
        // }
        // return GlobalResponse
        // .builder()
        // .message("Success")
        // .data(response)
        // .status(HttpStatus.OK)
        // .build();
        // } catch (DataException e) {
        // throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data
        // error");

        // } catch (Exception e) {
        // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server
        // error");
        // }

        // }

        // public GlobalResponse findOneByBranchId(Long id, int page, int size) {
        // try {
        // Branch set = branchRepository.findById(id)
        // .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK));
        // Page<User> response = pagUser.findByBranch(set, PageRequest.of(page, size));
        // if (response.isEmpty()) {
        // return GlobalResponse.builder().message("Data not
        // found").status(HttpStatus.OK)
        // .build();
        // }
        // return GlobalResponse
        // .builder()
        // .message("Success")
        // .data(response)
        // .status(HttpStatus.OK)
        // .build();
        // } catch (DataException e) {
        // throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data
        // error");

        // } catch (Exception e) {
        // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server
        // error");
        // }
        // }

        public GlobalResponse save(
                        @Valid UserDTO userDTO
        // List<Long> regionId,
        // List<Long> areaId,
        // List<Long> branchId
        ) {
                try {
                        Level levelId = Level.builder()
                                        .id(userDTO.getLevel_id())
                                        .build();

                        Role roleId = Role.builder()
                                        .id(userDTO.getRole_id())
                                        .build();

                        Main mainId = new Main();
                        if (userDTO.getMain_id() != null) {
                                Optional<Main> mainGet = mainRepository.findById(userDTO.getMain_id());
                                if (!mainGet.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("Main with id:" + mainGet.get().getId()
                                                                        + " is not found")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else {
                                        mainId = Main.builder().id(mainGet.get().getId()).build();
                                }
                        } else {
                                mainId = null;
                        }

                        List<Long> region = new ArrayList<>();
                        if (userDTO.getRegion_id() != null) {
                                for (int i = 0; i < userDTO.getRegion_id().size(); i++) {
                                        Optional<Region> getRegion = regionRepository
                                                        .findById(userDTO.getRegion_id().get(i));
                                        if (!getRegion.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Region with id:" + getRegion.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                region.add(getRegion.get().getId());
                                        }
                                }
                        }
                        List<Long> area = new ArrayList<>();
                        if (userDTO.getArea_id() != null) {
                                for (int i = 0; i < userDTO.getArea_id().size(); i++) {
                                        Optional<Area> getArea = areaRepository
                                                        .findById(userDTO.getArea_id().get(i));
                                        if (!getArea.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Area with id:" + getArea.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                region.add(getArea.get().getId());
                                        }
                                }
                        }
                        List<Long> branch = new ArrayList<>();
                        if (userDTO.getBranch_id() != null) {
                                for (int i = 0; i < userDTO.getBranch_id().size(); i++) {
                                        Optional<Branch> getBranch = branchRepository
                                                        .findById(userDTO.getBranch_id().get(i));
                                        if (!getBranch.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Branch with id:" + getBranch.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                region.add(getBranch.get().getId());
                                        }
                                }
                        }

                        User user = new User(
                                        null,
                                        roleId,
                                        levelId,
                                        mainId,
                                        region,
                                        area,
                                        branch,
                                        userDTO.getEmail(),
                                        userDTO.getNip(),
                                        userDTO.getUsername(),
                                        passwordEncoder.encode(userDTO.getPassword()),
                                        userDTO.getFullname(),
                                        userDTO.getInitial_name(),
                                        1,
                                        0,
                                        new Date(),
                                        new Date());

                        try {
                                Optional<User> checkEmail = userRepository.findByEmail(userDTO.getEmail());
                                Optional<User> checkUsername = userRepository.findByUsername(userDTO.getUsername());
                                if (checkEmail.isPresent()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Email already exist")
                                                        .status(HttpStatus.FOUND)
                                                        .build();
                                }
                                if (checkUsername.isPresent()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Username already exist")
                                                        .status(HttpStatus.FOUND)
                                                        .build();
                                }
                                userRepository.save(user);
                        } catch (SqlScriptException e) {
                                return GlobalResponse.builder().error(e).status(HttpStatus.BAD_REQUEST).build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (SqlScriptException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                }
        }

        public GlobalResponse edit(UserDTO userDTO, Long id) {
                try {

                        User userGet = userRepository.findById(id).get();

                        Level levelId = Level.builder()
                                        .id(userDTO.getLevel_id())
                                        .build();

                        Role roleId = Role.builder()
                                        .id(userDTO.getRole_id())
                                        .build();

                        Main mainId = new Main();
                        if (userDTO.getMain_id() != null) {
                                mainId = Main.builder()
                                                .id(userDTO.getMain_id())
                                                .build();
                        } else {
                                mainId = null;
                        }

                        List<Long> region = new ArrayList<>();
                        if (userDTO.getRegion_id() != null) {
                                for (int i = 0; i < userDTO.getRegion_id().size(); i++) {
                                        Region getRegion = regionRepository
                                                        .findById(userDTO.getRegion_id().get(i))
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Region not found"));
                                        if (getRegion != null) {
                                                region.add(getRegion.getId());
                                        }
                                }
                        }
                        List<Long> area = new ArrayList<>();
                        if (userDTO.getArea_id() != null) {
                                for (int i = 0; i < userDTO.getArea_id().size(); i++) {
                                        Area getArea = areaRepository
                                                        .findById(userDTO.getArea_id().get(i))
                                                        .orElseThrow(
                                                                        () -> new ResourceNotFoundException(
                                                                                        "Area not found"));
                                        if (getArea != null) {
                                                area.add(getArea.getId());
                                        }
                                }
                        }
                        List<Long> branch = new ArrayList<>();
                        if (userDTO.getBranch_id() != null) {
                                for (int i = 0; i < userDTO.getBranch_id().size(); i++) {
                                        Branch getBranch = branchRepository
                                                        .findById(userDTO.getBranch_id().get(i))
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Branch not found"));
                                        if (getBranch != null) {
                                                branch.add(getBranch.getId());
                                        }
                                }
                        }
                        User user = new User(
                                        id,
                                        roleId,
                                        levelId,
                                        mainId,
                                        region,
                                        area,
                                        branch,
                                        userDTO.getEmail(),
                                        userDTO.getNip(),
                                        userDTO.getUsername(),
                                        passwordEncoder.encode(userDTO.getPassword()),
                                        userDTO.getFullname(),
                                        userDTO.getInitial_name(),
                                        1,
                                        0,
                                        userGet.getCreated_at(),
                                        new Date());

                        try {
                                userRepository.save(user);
                        } catch (DataIntegrityViolationException e) {
                                return GlobalResponse
                                                .builder()
                                                .error(e)
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } catch (Exception e) {
                                return GlobalResponse
                                                .builder()
                                                .error(e)
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (SqlScriptException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                }
        }

        public GlobalResponse delete(Long id) {
                try {
                        Optional<User> getUser = userRepository.findById(id);

                        User user = getUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));
                        user.setIs_delete(1);
                        user.setIs_active(0);
                        user.setUpdated_at(new Date());
                        User response = userRepository.save(user);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public GlobalResponse changePassword(ChangePasswordDTO changePasswordDTO, String username) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        // String current =
                        // passwordEncoder.encode(changePasswordDTO.getCurrent_password());

                        boolean result = passwordEncoder.matches(changePasswordDTO.getCurrent_password(),
                                        getUser.getPassword());
                        if (result) {
                                User user = getUser;
                                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNew_password()));
                                user.setUpdated_at(new Date());
                                userRepository.save(user);
                        } else {
                                return GlobalResponse
                                                .builder()
                                                .message("Password invalid")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public GlobalResponse changeProfile(ChangeProfileDTO dto, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                        if (dto.getUsername() == getUser.getUsername() || dto.getEmail() == getUser.getEmail()) {
                                return GlobalResponse
                                                .builder()
                                                .message("User already exist")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        User user = getUser;
                        user.setUsername(dto.getUsername());
                        user.setEmail(dto.getEmail());

                        try {
                                userRepository.save(user);
                        } catch (Exception e) {
                                return GlobalResponse.builder().error(e).status(HttpStatus.BAD_REQUEST).build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

}
