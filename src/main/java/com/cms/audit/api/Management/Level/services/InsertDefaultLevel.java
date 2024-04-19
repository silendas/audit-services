package com.cms.audit.api.Management.Level.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Level.repository.LevelRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InsertDefaultLevel {

    @Autowired
    private LevelRepository repository;

    @PostConstruct
    public void insertDefaultLevel() {
        List<Level> response = repository.findAll();
        if (!response.isEmpty()) {
            return;
        }
        Level level1 = new Level(
                null,
                "PUSAT",
                "A",
                0,
                new Date(),
                new Date());
        repository.save(level1);
        Level level2 = new Level(
                null,
                "AREA",
                "B",
                0,
                new Date(),
                new Date());
        repository.save(level2);
        Level level3 = new Level(
                null,
                "WILAYAH",
                "C",
                0,
                new Date(),
                new Date());
        repository.save(level3);
    }

}
