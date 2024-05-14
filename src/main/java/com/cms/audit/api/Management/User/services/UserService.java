package com.cms.audit.api.Management.User.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.MainOffice.repository.MainRepository;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.User.dto.ChangePasswordDTO;
import com.cms.audit.api.Management.User.dto.ChangeProfileDTO;
import com.cms.audit.api.Management.User.dto.DropDownUserDTO;
import com.cms.audit.api.Management.User.dto.EditUserDTO;
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
        private LogUserService logService;

        @Autowired
        private AreaRepository areaRepository;

        @Autowired
        private BranchRepository branchRepository;

        @Autowired
        private BranchService branchService;

        @Autowired
        private PagUser pagUser;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public GlobalResponse findAll(int page, int size, String username) {
                User getUser = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "user with username " + username + " is not found"));
                Pageable pageable = PageRequest.of(page, size);
                List<User> user = new ArrayList<>();
                if (getUser.getLevel().getCode().equals("A")) {
                        user = userRepository.findAllUser();
                } else if (getUser.getLevel().getCode().equals("B")) {
                        if (getUser.getRegionId() != null) {
                                List<User> userAgain = userRepository.findAllUser();
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
                                                                                if (!user.contains(userAgain.get(u))) {
                                                                                        user.add(userAgain.get(u));
                                                                                }
                                                                        }

                                                                }
                                                        }
                                                } else {
                                                        for (int o = 0; o < userAgain.get(u).getRegionId()
                                                                        .size(); o++) {
                                                                if (regionId == userAgain.get(u).getRegionId().get(o)) {
                                                                        if (!user.contains(userAgain.get(u))) {
                                                                                user.add(userAgain.get(u));
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        } else {
                                List<User> userAgain = userRepository.findAllUser();
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

                                                                                        if (!user.contains(userAgain
                                                                                                        .get(u))) {
                                                                                                user.add(userAgain.get(
                                                                                                                u));
                                                                                        }
                                                                                        break;
                                                                                }
                                                                        }
                                                                }
                                                        } else {
                                                                for (int o = 0; o < userAgain.get(u).getRegionId()
                                                                                .size(); o++) {
                                                                        if (regionId == userAgain.get(u).getRegionId()
                                                                                        .get(o)) {
                                                                                if (!user.contains(userAgain.get(u))) {
                                                                                        user.add(userAgain.get(u));
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                        lastId = regionId;
                                }
                        }
                } else if (getUser.getLevel().getCode().equals("C")) {
                        return GlobalResponse.builder().message("Audit wilayah tidak dapat akses")
                                        .status(HttpStatus.BAD_REQUEST).build();
                }
                try {
                        List<UserResponse> listResponses = pageUserDetail(user);
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()), listResponses.size());
                        List<UserResponse> pageContent = listResponses.subList(start, end);
                        Page<UserResponse> response = new PageImpl<>(pageContent, pageable, listResponses.size());
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil meanmpilkan data")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Data tidak ditemukan")
                                        .data(user)
                                        .status(HttpStatus.OK)
                                        .build();
                }
        }

        public List<UserResponse> pageUserDetail(List<User> response) {

                List<UserResponse> listUser = new ArrayList<>();
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

                        UserResponse user = new UserResponse();
                        user.setId(response.get(u).getId());
                        user.setLevel(response.get(u).getLevel());
                        user.setMain(response.get(u).getMain());
                        user.setRegion(region);
                        user.setArea(area);
                        user.setBranch(branch);
                        user.setEmail(response.get(u).getEmail());
                        user.setUsername(response.get(u).getUsername());
                        user.setFullname(response.get(u).getFullname());
                        user.setInitial_name(response.get(u).getInitial_name());
                        user.setNip(response.get(u).getNip());
                        user.setIs_active(response.get(u).getIs_active());
                        user.setCreated_at(response.get(u).getCreated_at());
                        user.setUpdated_at(response.get(u).getUpdated_at());

                        listUser.add(user);

                }

                return listUser;
        }

        public GlobalResponse findOne(Long id) {
                try {
                        Optional<User> response = userRepository.findById(id);
                        if (!response.isPresent()) {
                                return GlobalResponse.builder().message("Data tidak ditemukan").status(HttpStatus.BAD_REQUEST)
                                                .data(response)
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
                                        .message("Berhasil menampilkan data")
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
                                return GlobalResponse.builder().message("Data tidak ditemukan").status(HttpStatus.BAD_REQUEST)
                                                .data(response)
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
                                        .message("Berhasil menampilkan data")
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
                                if (response.isEmpty()) {
                                        return GlobalResponse.builder().message("Data tidak ditemukan").status(HttpStatus.OK)
                                                        .data(response).build();
                                }
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasilmenampilkan data")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public List<Object> listObjectDropdown(List<DropDownUser> dto) {
                List<Object> response = new ArrayList<>();
                for (int i = 0; i < dto.size(); i++) {
                        Map<String, Object> createList = new LinkedHashMap<>();
                        createList.put("id", dto.get(i).getId());
                        createList.put("fullname", dto.get(i).getFullname());
                        createList.put("initial_name", dto.get(i).getInitial_Name());
                        Optional<User> getUser = userRepository.findById(dto.get(i).getId());
                        if (getUser.get().getRegionId().isEmpty()) {
                                // List<Object> getBranch = reg
                                // if(!getBranch.isEmpty()){
                                // createList.put("branch", getBranch);
                                // }
                        } else if (getUser.get().getBranchId().isEmpty()) {
                                List<Object> getBranch = branchService.findByUser(dto.get(i).getId());
                                if (!getBranch.isEmpty()) {
                                        createList.put("branch", getBranch);
                                }
                        }
                }
                return response;
        }

        public GlobalResponse dropDown(Long id) {
                try {
                        List<User> response = userRepository.findUserExceptItSelf(id);
                        List<Object> listResponse = new ArrayList<>();
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("Data tidak ditemukan").data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        for (int i = 0; i < response.size(); i++) {
                                Map<String, Object> oneUser = new LinkedHashMap<>();
                                oneUser.put("id", response.get(i).getId());
                                oneUser.put("fullname", response.get(i).getFullname());
                                oneUser.put("initial_name", response.get(i).getInitial_name());
                                oneUser.put("level", response.get(i).getLevel());
                                if (!response.get(i).getBranchId().isEmpty()) {
                                        List<Object> listBranch = new ArrayList<>();
                                        for (int u = 0; u < response.get(i).getBranchId().size(); u++) {
                                                Optional<Branch> getBranch = branchRepository
                                                                .findById(response.get(i).getBranchId().get(u));
                                                if (getBranch.isPresent()) {
                                                        Map<String, Object> objBranch = new LinkedHashMap<>();
                                                        objBranch.put("id", getBranch.get().getId());
                                                        objBranch.put("name", getBranch.get().getName());                                                        
                                                        objBranch.put("area", getBranch.get().getArea()); 
                                                        listBranch.add(objBranch);                                                       
                                                }
                                        }
                                        oneUser.put("office", listBranch);
                                } else if (!response.get(i).getRegionId().isEmpty()) {
                                        List<Region> listRegion = new ArrayList<>();
                                        for (int u = 0; u < response.get(i).getRegionId().size(); u++) {
                                                Optional<Region> getRegion = regionRepository
                                                                .findById(response.get(i).getRegionId().get(u));
                                                if (getRegion.isPresent()) {
                                                        listRegion.add(getRegion.get());
                                                }
                                        }
                                        oneUser.put("office", listRegion);
                                } else {
                                        List<Main> list = new ArrayList<>();
                                        list.add(response.get(i).getMain());
                                        oneUser.put("office", list);
                                }
                                listResponse.add(oneUser);
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(listResponse)
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
                        List<User> getUser = userRepository.findAllUser();
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
                                return GlobalResponse.builder().message("Data tidak ditemukan").data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
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
                                return GlobalResponse.builder().message("Data tidak ditemukan").status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public GlobalResponse save(
                        @Valid UserDTO userDTO) {
                try {

                        Level levelId = Level.builder()
                                        .id(userDTO.getLevel_id())
                                        .build();

                        Role roleId = Role.builder()
                                        .id(1L)
                                        .build();

                        if (userDTO.getLevel_id() == 1 || userDTO.getLevel_id() == 4) {
                                if (userDTO.getMain_id() == null) {
                                        return GlobalResponse.builder().message("Main office harus diis")
                                                        .errorMessage("Main id tidak boleh kosong")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else if (userDTO.getBranch_id() != null || userDTO.getRegion_id() != null
                                                || userDTO.getArea_id() != null) {
                                        return GlobalResponse.builder().message("Hanya main office")
                                                        .errorMessage("Hanya main id saja yang diisi")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                }
                        } else if (userDTO.getLevel_id() == 2) {
                                if (userDTO.getRegion_id() == null) {
                                        return GlobalResponse.builder().message("Region id harus diisi")
                                                        .errorMessage("Region id tidak boleh kosong")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else if (userDTO.getBranch_id() != null || userDTO.getMain_id() != null
                                                || userDTO.getArea_id() != null) {
                                        return GlobalResponse.builder().message("Hanya region office")
                                                        .errorMessage("Hanya REgion id saja yang diisi")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                }
                        } else if (userDTO.getLevel_id() == 3) {
                                if (userDTO.getBranch_id() == null || userDTO.getArea_id() == null) {
                                        return GlobalResponse.builder()
                                                        .message("Branch harus diisi")
                                                        .errorMessage("Branch id dan Area id tidak boleh kosong")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else if (userDTO.getMain_id() != null || userDTO.getRegion_id() != null) {
                                        return GlobalResponse.builder().message("hanya branch office")
                                                        .errorMessage("Hanya branch id dan area id saja yang diisi")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                }
                        }
                        Main mainId = new Main();
                        if (userDTO.getMain_id() != null) {
                                Optional<Main> mainGet = mainRepository.findById(userDTO.getMain_id());
                                if (!mainGet.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("main office tidak ada")
                                                        .errorMessage("Main with id:" + mainGet.get().getId()
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
                                        if (userDTO.getRegion_id().equals(region)) {
                                                continue;
                                        }
                                        Optional<Region> getRegion = regionRepository
                                                        .findById(userDTO.getRegion_id().get(i));
                                        if (!getRegion.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Region office tidak ada")
                                                                .errorMessage("Region with id:"
                                                                                + getRegion.get().getId()
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
                                        if (userDTO.getArea_id().equals(area)) {
                                                continue;
                                        }
                                        Optional<Area> getArea = areaRepository
                                                        .findById(userDTO.getArea_id().get(i));
                                        if (!getArea.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Area office tidak ada")
                                                                .errorMessage("Area with id:" + getArea.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                area.add(getArea.get().getId());
                                        }
                                }
                        }
                        List<Long> branch = new ArrayList<>();
                        if (userDTO.getBranch_id() != null) {
                                for (int i = 0; i < userDTO.getBranch_id().size(); i++) {
                                        if (userDTO.getBranch_id().equals(branch)) {
                                                continue;
                                        }
                                        Optional<Branch> getBranch = branchRepository
                                                        .findById(userDTO.getBranch_id().get(i));
                                        if (!getBranch.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Branch office tidak ada")
                                                                .errorMessage("Branch with id:"
                                                                                + getBranch.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                branch.add(getBranch.get().getId());
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
                                List<User> checkNip = userRepository.findByNIP(userDTO.getNip());
                                List<User> checkIN = userRepository.findByInitialName(userDTO.getInitial_name());
                                Optional<User> checkUsername = userRepository.findByUsername(userDTO.getUsername());
                                if (checkEmail.isPresent()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("email sudah ada")
                                                        .errorMessage("Email already exist")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                if (!checkNip.isEmpty()) {
                                        return GlobalResponse
                                                        .builder().message("NIP sudah ada")
                                                        .errorMessage("NIP already exist")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                if (!checkIN.isEmpty()) {
                                        return GlobalResponse
                                                        .builder().message("Initial name sudah ada")
                                                        .errorMessage("Initial Name already exist")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                if (checkUsername.isPresent()) {
                                        return GlobalResponse
                                                        .builder().message("Username sudah ada")
                                                        .errorMessage("Username already exist")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                User response = userRepository.save(user);
                                logService.insertAuto(response);
                        } catch (SqlScriptException e) {
                                return GlobalResponse.builder().error(e).status(HttpStatus.BAD_REQUEST).build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menambahkan data user")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (

                SqlScriptException e) {
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

        public GlobalResponse edit(EditUserDTO userDTO, Long id) {
                try {

                        Optional<User> userGet = userRepository.findById(id);
                        if (!userGet.isPresent()) {
                                return GlobalResponse.builder().errorMessage("User tidak ditemukan")
                                                .message("User dengan id : " + id + " tidak ditemukan").build();
                        }

                        String password = null;
                        if (!userDTO.getPassword().equals("") && userDTO.getPassword() != null) {
                                password = passwordEncoder.encode(userDTO.getPassword());
                        } else {
                                password = userGet.get().getPassword();
                        }

                        Level levelId = Level.builder()
                                        .id(userDTO.getLevel_id())
                                        .build();

                        Role roleId = Role.builder()
                                        .id(1L)
                                        .build();

                        if (userDTO.getLevel_id() == 1 || userDTO.getLevel_id() == 4) {
                                if (userDTO.getMain_id() == null) {
                                        return GlobalResponse.builder().message("Main office harus diis")
                                                        .errorMessage("Main id tidak boleh kosong")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else if (userDTO.getBranch_id() != null || userDTO.getRegion_id() != null
                                                || userDTO.getArea_id() != null) {
                                        return GlobalResponse.builder().message("Hanya main office")
                                                        .errorMessage("Hanya main id saja yang diisi")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                }
                        } else if (userDTO.getLevel_id() == 2) {
                                if (userDTO.getRegion_id() == null) {
                                        return GlobalResponse.builder().message("Region id harus diisi")
                                                        .errorMessage("Region id tidak boleh kosong")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else if (userDTO.getBranch_id() != null || userDTO.getMain_id() != null
                                                || userDTO.getArea_id() != null) {
                                        return GlobalResponse.builder().message("Hanya region office")
                                                        .errorMessage("Hanya REgion id saja yang diisi")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                }
                        } else if (userDTO.getLevel_id() == 3) {
                                if (userDTO.getBranch_id() == null || userDTO.getArea_id() == null) {
                                        return GlobalResponse.builder()
                                                        .message("Branch harus diisi")
                                                        .errorMessage("Branch id dan Area id tidak boleh kosong")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                } else if (userDTO.getMain_id() != null || userDTO.getRegion_id() != null) {
                                        return GlobalResponse.builder().message("hanya branch office")
                                                        .errorMessage("Hanya branch id dan area id saja yang diisi")
                                                        .status(HttpStatus.BAD_REQUEST).build();
                                }
                        }
                        Main mainId = new Main();
                        if (userDTO.getMain_id() != null) {
                                Optional<Main> mainGet = mainRepository.findById(userDTO.getMain_id());
                                if (!mainGet.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("main office tidak ada")
                                                        .errorMessage("Main with id:" + mainGet.get().getId()
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
                                        if (userDTO.getRegion_id().equals(region)) {
                                                continue;
                                        }
                                        Optional<Region> getRegion = regionRepository
                                                        .findById(userDTO.getRegion_id().get(i));
                                        if (!getRegion.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Region office tidak ada")
                                                                .errorMessage("Region with id:"
                                                                                + getRegion.get().getId()
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
                                        if (userDTO.getArea_id().equals(area)) {
                                                continue;
                                        }
                                        Optional<Area> getArea = areaRepository
                                                        .findById(userDTO.getArea_id().get(i));
                                        if (!getArea.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Area office tidak ada")
                                                                .errorMessage("Area with id:" + getArea.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                area.add(getArea.get().getId());
                                        }
                                }
                        }
                        List<Long> branch = new ArrayList<>();
                        if (userDTO.getBranch_id() != null) {
                                for (int i = 0; i < userDTO.getBranch_id().size(); i++) {
                                        if (userDTO.getBranch_id().equals(branch)) {
                                                continue;
                                        }
                                        Optional<Branch> getBranch = branchRepository
                                                        .findById(userDTO.getBranch_id().get(i));
                                        if (!getBranch.isPresent()) {
                                                return GlobalResponse.builder()
                                                                .message("Branch office tidak ada")
                                                                .errorMessage("Branch with id:"
                                                                                + getBranch.get().getId()
                                                                                + " is not found")
                                                                .status(HttpStatus.BAD_REQUEST).build();
                                        } else {
                                                branch.add(getBranch.get().getId());
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
                                        password,
                                        userDTO.getFullname(),
                                        userDTO.getInitial_name(),
                                        1,
                                        0,
                                        userGet.get().getCreated_at(),
                                        new Date());
                        try {
                                if (!user.getEmail().equals(userDTO.getEmail())) {
                                        Optional<User> checkEmail = userRepository.findByEmail(userDTO.getEmail());
                                        if (checkEmail.isPresent()) {
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Email already exist")
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                }
                                if (!user.getNip().equals(userDTO.getNip())) {
                                        List<User> checkNip = userRepository.findByNIP(userDTO.getNip());
                                        if (!checkNip.isEmpty()) {
                                                return GlobalResponse
                                                                .builder()
                                                                .message("NIP already exist")
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                }
                                if (!user.getInitial_name().equals(userDTO.getInitial_name())) {
                                        List<User> checkIN = userRepository
                                                        .findByInitialName(userDTO.getInitial_name());
                                        if (!checkIN.isEmpty()) {
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Initial Name already exist")
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                }
                                if (!user.getUsername().equals(userDTO.getUsername())) {
                                        Optional<User> checkUsername = userRepository
                                                        .findByUsername(userDTO.getUsername());
                                        if (checkUsername.isPresent()) {
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Username already exist")
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                }
                                User response = userRepository.save(user);
                                logService.insertAuto(response);
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
                                        .message("Berhasil mengubah data user")
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
                        if (!getUser.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("User tidak ditemukan")
                                                .errorMessage("User with id ;" + id + " not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        User user = getUser.get();
                        user.setIs_delete(1);
                        user.setIs_active(0);
                        user.setUpdated_at(new Date());
                        User response = userRepository.save(user);
                        logService.insertAuto(response);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menghapus data user")
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
                                User response = userRepository.save(user);
                                logService.insertAuto(response);
                        } else {
                                return GlobalResponse
                                                .builder()
                                                .message("Password invalid")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil mengubah password")
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

                        Optional<User> checkEmail = userRepository.findByEmail(dto.getEmail());
                        if(checkEmail.isPresent()) {
                                return GlobalResponse
                                                .builder()      
                                                .message("Email sudah tesedia")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        User user = getUser;
                        user.setFullname(dto.getFullname());
                        user.setEmail(dto.getEmail());

                        try {
                                User response = userRepository.save(user);
                                logService.insertAuto(response);
                        } catch (Exception e) {
                                return GlobalResponse.builder().error(e).status(HttpStatus.BAD_REQUEST).build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil mengubah profile user")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

}
