package com.cms.audit.api.Management.User.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InsertDevUser {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void insertDefaultRole() {
        List<User> response = repository.findAll();
        if (!response.isEmpty()) {
            return;
        }
        List<Long> list = new ArrayList<>();
        User user = new User();
        user.setMain(null);
        user.setAreaId(list);
        user.setRegionId(list);
        user.setBranchId(list);
        user.setRole(Role.builder().id(Long.valueOf(2)).build());
        user.setLevel(Level.builder().id(Long.valueOf(1)).build());
        user.setEmail("dev@gmail.com");
        user.setUsername("developer");
        user.setPassword(passwordEncoder.encode("developer123"));
        user.setInitial_name("DEV");
        user.setNip("DEV1");
        user.setIs_active(1);
        user.setIs_delete(0);
        user.setUpdated_at(new Date());
        user.setCreated_at(new Date());
        repository.save(user);
    }

}
