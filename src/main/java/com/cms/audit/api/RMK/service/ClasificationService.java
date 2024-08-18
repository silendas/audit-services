package com.cms.audit.api.RMK.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.RMK.dto.ClasificationDto;
import com.cms.audit.api.RMK.model.Clasification;
import com.cms.audit.api.RMK.model.ClasificationCategory;
import com.cms.audit.api.RMK.repository.ClasificationRepo;
import com.cms.audit.api.RMK.repository.PagClasification;

@Service
public class ClasificationService {

    @Autowired
    private ClasificationRepo repo;

    @Autowired
    private PagClasification pag;

    public ResponseEntity<Object> getClasification(boolean pageable, int page, int size) {
        Specification<Clasification> spec = Specification
                .where(new SpecificationFIlter<Clasification>().isNotDeleted())
                .and(new SpecificationFIlter<Clasification>().orderByIdDesc());
        if (pageable) {
            return ResponseEntittyHandler.allHandler(pag.findAll(spec, PageRequest.of(page, size)), "Berhasil", HttpStatus.OK, null);
        } else {
            return ResponseEntittyHandler.allHandler(repo.findAll(spec), "Berhasil", HttpStatus.OK, null);
        }
    }

    public ResponseEntity<Object> createClasification(ClasificationDto clasification) {
        if (repo.existsByName(clasification.getName())) {
            return ResponseEntittyHandler.errorResponse("Name sudah ada", "Name sudah ada", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntittyHandler.allHandler(repo.save(builderClasification(clasification)), "Berhasil", HttpStatus.CREATED, null);
    }

    public ResponseEntity<Object> getCategory() {
        return ResponseEntittyHandler.allHandler(ClasificationCategory.values(), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> updateClasification(ClasificationDto clasification, Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan", HttpStatus.NOT_FOUND);
        }
        if (repo.existsByName(clasification.getName())) {
            return ResponseEntittyHandler.errorResponse("Name sudah ada", "Name sudah ada", HttpStatus.BAD_REQUEST);
        }
        Clasification response = repo.findById(id).get();
        response.setName(clasification.getName());
        response.setCategory(clasification.getCategory());
        response.setUpdated_at(new Date());
        return ResponseEntittyHandler.allHandler(repo.save(response), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteClasification(Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan", HttpStatus.NOT_FOUND);
        }
        Clasification response = repo.findById(id).get();
        response.setIs_delete(1);
        response.setUpdated_at(new Date());
        return ResponseEntittyHandler.allHandler(repo.save(response), "Berhasil", HttpStatus.OK, null);
    }

    public Clasification builderClasification(ClasificationDto clasification) {
        Clasification response = new Clasification();
        response.setName(clasification.getName());
        response.setCategory(clasification.getCategory());
        response.setIs_delete(0);
        response.setCreated_at(new Date());
        response.setUpdated_at(new Date());
        return response;
    }

    public Clasification getClasificationById(Long id){
        return repo.findById(id).get();
    }

    public List<Clasification> getAllClasification(){
        return repo.findAll();
    }

}
