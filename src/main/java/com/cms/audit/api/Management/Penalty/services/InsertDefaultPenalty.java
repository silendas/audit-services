package com.cms.audit.api.Management.Penalty.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InsertDefaultPenalty {

    @Autowired
    private PenaltyRepository repository;

    @PostConstruct
    public void insertDefault() {
        List<Penalty> response = repository.findAll();
        if (!response.isEmpty()) {
            return;
        }
        Penalty penalty1 = new Penalty(
                null,
                "SP1",
                0,
                new Date(),
                new Date());
        repository.save(penalty1);
        Penalty penalty2 = new Penalty(
                null,
                "SP2",
                0,
                new Date(),
                new Date());
        repository.save(penalty2);
        Penalty penalty3 = new Penalty(
                null,
                "SP3",
                0,
                new Date(),
                new Date());
        repository.save(penalty3);
        Penalty penalty4 = new Penalty(
                null,
                "Surat Pembebanan/PG",
                0,
                new Date(),
                new Date());
        repository.save(penalty4);
    }

}
