package com.cms.audit.api.Management.Role.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Role.models.Role;
import com.cms.audit.api.Management.Role.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InsertDefaultRole {

    @Autowired
    private RoleRepository repository;

    @PostConstruct
    public void insertDefaultRole() {
        List<Role> response = repository.findAll();
        if (!response.isEmpty()) {
            return;
        }
        Role role1 = new Role(
                null,
                "USER",
                0,
                new Date(),
                new Date());
        repository.save(role1);
        Role role2 = new Role(
                null,
                "ADMINISTRATOR",
                0,
                new Date(),
                new Date());
        repository.save(role2);
    }

}
