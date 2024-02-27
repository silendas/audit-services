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
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserProfileRepository;
import com.cms.audit.api.Management.User.response.Response;
import com.cms.audit.api.Management.User.response.UserProfileInterface;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserProfileService {
        @Autowired
        private UserProfileRepository userProfileRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public Response findAll() {
                try {
                        List<UserProfileInterface> userResponse = userProfileRepository.findAllUserProfile();
                        if (userResponse.isEmpty()) {
                                return Response
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
                                                .build();
                        }
                        return Response
                                        .builder()
                                        .message("Success")
                                        .data(userResponse)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public Response findOne(Long id) {
                try {
                        List<UserProfileInterface> userResponse = userProfileRepository.findOneUserProfileById(id);
                        if (userResponse.isEmpty()) {
                                return Response
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
                                                .build();
                        }
                        return Response
                                        .builder()
                                        .message("Success")
                                        .data(userResponse)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public Response findOneByMainId(Long id) {
                try {
                        List<UserProfileInterface> userResponse = userProfileRepository.findOneUserProfileByMainId(id);
                        if (userResponse.isEmpty()) {
                                return Response
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
                                                .build();
                        }
                        return Response
                                        .builder()
                                        .message("Success")
                                        .data(userResponse)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public Response save(UserDTO userDTO) {
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

                        User userResponse = userProfileRepository.save(user);
                        if (userResponse == null) {
                                return Response
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        return Response
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public Response edit(UserDTO userDTO) {
                try {

                        User userGet = userProfileRepository.findById(userDTO.getId()).get();
                        if (userGet == null) {
                                return Response
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

                        User userResponse = userProfileRepository.save(user);
                        if (userResponse == null) {
                                return Response
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        return Response
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return Response
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

}
