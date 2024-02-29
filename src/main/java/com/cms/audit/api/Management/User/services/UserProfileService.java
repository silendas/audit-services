package com.cms.audit.api.Management.User.services;

import java.util.Date;
import java.util.List;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.MainOffice.models.Main;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.dto.response.UserProfileInterface;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserProfileRepository;
import com.cms.audit.api.common.response.GlobalResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserProfileService {
        @Autowired
        private UserProfileRepository userProfileRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public GlobalResponse findAll() {
                try {
                        List<UserProfileInterface> response = userProfileRepository.findAllUserProfile();
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse findOne(Long id) {
                try {
                        List<UserProfileInterface> response = userProfileRepository.findOneUserProfileById(id);
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse findOneByMainId(Long id) {
                try {
                        List<UserProfileInterface> response = userProfileRepository.findOneUserProfileByMainId(id);
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
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
                                        userDTO.getFull_name(),
                                        userDTO.getInitial_name(),
                                        1,
                                        0,
                                        new Date(),
                                        new Date());

                        User response = userProfileRepository.save(user);
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
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse edit(UserDTO userDTO) {
                try {

                        User userGet = userProfileRepository.findById(userDTO.getId()).get();
                        if (userGet == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
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

                        int is_active;

                        if (userDTO.getIs_delete() == 1) {
                                is_active = 0;
                        }
                        is_active = 1;

                        User user = new User(
                                        userDTO.getId(),
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
                                        userDTO.getFull_name(),
                                        userDTO.getInitial_name(),
                                        is_active,
                                        userDTO.getIs_delete(),
                                        userGet.getCreated_at(),
                                        new Date());

                        User response = userProfileRepository.save(user);
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
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

}
