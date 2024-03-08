package com.cms.audit.api.Management.User.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.User.dto.ChangePasswordDTO;
import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.dto.response.DropDownUser;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.common.response.GlobalResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public GlobalResponse findAll() {
                try {
                        List<User> response = userRepository.findAll();
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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

        public GlobalResponse findOne(Long id) {
                try {
                        Optional<User> response = userRepository.findById(id);
                        if (!response.isPresent()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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

        public GlobalResponse findOneByMainId(Long id) {
                try {
                        List<User> response = userRepository.findUserByMain(id);
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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

        public GlobalResponse findOneByRegionId(Long id) {
                try {
                        List<User> response = userRepository.findUserByRegion(id);
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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
        
        public GlobalResponse dropDownByRegionId(Long id) {
                try {
                        List<DropDownUser> response = userRepository.findDropDownByRegion(id);
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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

        public GlobalResponse findOneByAreaId(Long id) {
                try {
                        List<User> response = userRepository.findUserByArea(id);
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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

        public GlobalResponse findOneByBranchId(Long id) {
                try {
                        List<User> response = userRepository.findUserByBranch(id);
                        if (response.isEmpty()) {
                                return GlobalResponse.builder().message("No Content").status(HttpStatus.NO_CONTENT).build();
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

        public GlobalResponse save(UserDTO userDTO) {
                try {
                        Level levelId = Level.builder()
                                        .id(userDTO.getLevel_id())
                                        .build();

                        Role roleId = Role.builder()
                                        .id(userDTO.getRole_id())
                                        .build();

                        Main mainId = Main.builder()
                                        .id(userDTO.getMain_id())
                                        .build();

                        Region regionId = Region.builder()
                                        .id(userDTO.getRegion_id())
                                        .build();

                        Area areaId = Area.builder()
                                        .id(userDTO.getArea_id())
                                        .build();

                        Branch branchId = Branch.builder()
                                        .id(userDTO.getBranch_id())
                                        .build();

                        User user = new User(
                                        null,
                                        roleId,
                                        levelId,
                                        mainId,
                                        regionId,
                                        areaId,
                                        branchId,
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

                        User response = userRepository.save(user);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (SqlScriptException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Sql error");
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");

                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

        public GlobalResponse edit(UserDTO userDTO, Long id) {
                try {

                        User userGet = userRepository.findById(id).get();
                        if (userGet == null) {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found User");
                        }

                        Level levelId = Level.builder()
                                        .id(userDTO.getLevel_id())
                                        .build();

                        Role roleId = Role.builder()
                                        .id(userDTO.getRole_id())
                                        .build();

                        Main mainId = Main.builder()
                                        .id(userDTO.getMain_id())
                                        .build();

                        Region regionId = Region.builder()
                                        .id(userDTO.getRegion_id())
                                        .build();

                        Area areaId = Area.builder()
                                        .id(userDTO.getArea_id())
                                        .build();

                        Branch branchId = Branch.builder()
                                        .id(userDTO.getBranch_id())
                                        .build();

                        User user = new User(
                                        id,
                                        roleId,
                                        levelId,
                                        mainId,
                                        regionId,
                                        areaId,
                                        branchId,
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

        public GlobalResponse delete(Long id) {
                try {
                        Optional<User> getUser = userRepository.findById(id);

                        User user = new User(
                                id, 
                                getUser.get().getRole(), 
                                getUser.get().getLevel(), 
                                getUser.get().getMain(), 
                                getUser.get().getRegion(), 
                                getUser.get().getArea(), 
                                getUser.get().getBranch(), 
                                getUser.get().getEmail(), 
                                getUser.get().getNip(), 
                                getUser.get().getUsername(), 
                                getUser.get().getPassword(), 
                                getUser.get().getFullname(), 
                                getUser.get().getInitial_name(), 
                                0, 
                                1, 
                                getUser.get().getCreated_at(), 
                                new Date());

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

        public GlobalResponse changePassword(ChangePasswordDTO changePasswordDTO, Long id) {
                try {

                        Optional<User> getUser = userRepository.findById(id);

                        User user = new User(
                                id, 
                                getUser.get().getRole(), 
                                getUser.get().getLevel(), 
                                getUser.get().getMain(), 
                                getUser.get().getRegion(), 
                                getUser.get().getArea(), 
                                getUser.get().getBranch(), 
                                getUser.get().getEmail(), 
                                getUser.get().getNip(), 
                                getUser.get().getUsername(), 
                                passwordEncoder.encode(changePasswordDTO.getPassword()), 
                                getUser.get().getFullname(), 
                                getUser.get().getInitial_name(), 
                                1, 
                                0, 
                                getUser.get().getCreated_at(), 
                                new Date());

                        User response = userRepository.save(user);
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
