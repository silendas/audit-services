package com.cms.audit.api.Management.User.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.Management.User.dto.ChangePasswordDTO;
import com.cms.audit.api.Management.User.dto.ChangeProfileDTO;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.services.UserService;

import io.micrometer.common.lang.NonNull;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_PROFILE)
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Object> profile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GlobalResponse response = userService.findOne(user.getId());
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PatchMapping("/change-profile")
    public ResponseEntity<Object> edit(
            @NonNull HttpServletRequest request,
            @Nullable @RequestBody ChangeProfileDTO userDTO) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = userService.changeProfile(userDTO, username);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Object> changePassword(
            @Nonnull HttpServletRequest request,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = userService.changePassword(changePasswordDTO, username);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

}
