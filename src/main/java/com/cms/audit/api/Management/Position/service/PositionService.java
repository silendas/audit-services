package com.cms.audit.api.Management.Position.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.Level.models.Level;
import com.cms.audit.api.Management.Level.repository.LevelRepository;
import com.cms.audit.api.Management.Position.dto.PositionDTO;
import com.cms.audit.api.Management.Position.models.Position;
import com.cms.audit.api.Management.Position.repository.PositionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PositionService {

    @Autowired
    private PositionRepository repository;

    @Autowired
    private LevelRepository levelRepository;

    public ResponseEntity<Object> getAll() {
        try {
            List<Position> response = repository.findAllPosition();
            String message;
            if (response.isEmpty()) {
                message = "Tidak ada data";
            } else {
                message = "Berhasil menampilkan data";
            }
            return ResponseEntittyHandler.allHandler(repository.findAll(), message, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseEntittyHandler.allHandler(null, "Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> getById(Long id) {
        try {
            Optional<Position> response = repository.findById(id);
            String message;
            if (response.isPresent()) {
                message = "Berhasil menemukan data";
            } else {
                return ResponseEntittyHandler.errorResponse("Data tidak ditemukan","Data with id ;" + id + " not found",HttpStatus.BAD_REQUEST);
            }
            return ResponseEntittyHandler.allHandler(response, message, HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseEntittyHandler.allHandler(null, "Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> create(PositionDTO position) {
        try {
            if(position.getName() == null) {
                return ResponseEntittyHandler.errorResponse("Data name tidak boleh kosong","Data name cannot be empty",HttpStatus.BAD_REQUEST);
            }
            Position position1 = new Position();
            position1.setName(position.getName());
            Optional<Level> level = levelRepository.findById(position.getLevel_id());
            if(!level.isPresent()) {
                return ResponseEntittyHandler.errorResponse("Data tidak ditemukan","Data with id ;" + position.getLevel_id() + " not found",HttpStatus.BAD_REQUEST);
            }
            position1.setLevel(level.get());
            repository.save(position1);
            return ResponseEntittyHandler.allHandler(null, "Berhasil menambahkan data", HttpStatus.CREATED, null);
        } catch (Exception e) {
            return ResponseEntittyHandler.allHandler(null, "Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> update(PositionDTO position, Long id) {
        try {
            Optional<Position> response = repository.findById(id);
            if(!response.isPresent()) {
                return ResponseEntittyHandler.errorResponse("Data tidak ditemukan","Data with id ;" + id + " not found",HttpStatus.BAD_REQUEST);
            }
            Position position1 = response.get();
            position1.setName(position.getName());
            Optional<Level> level = levelRepository.findById(position.getLevel_id());
            if(!level.isPresent()) {
                return ResponseEntittyHandler.errorResponse("Data level tidak ditemukan","Data with id ;" + position.getLevel_id() + " not found",HttpStatus.BAD_REQUEST);
            }
            position1.setLevel(level.get());
            repository.save(position1);
            return ResponseEntittyHandler.allHandler(null, "Berhasil mengubah data", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseEntittyHandler.allHandler(null, "Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> delete(Long id) {
        try {
            Optional<Position> response = repository.findById(id);
            if(response.isPresent()) {
                return ResponseEntittyHandler.errorResponse("Data tidak ditemukan","Data with id ;" + id + " not found",HttpStatus.BAD_REQUEST);
            }
            Position position = response.get();
            position.setIs_delete(1);
            repository.save(position);
            return ResponseEntittyHandler.allHandler(null, "Berhasil menghapus data", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseEntittyHandler.allHandler(null, "Error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

}
