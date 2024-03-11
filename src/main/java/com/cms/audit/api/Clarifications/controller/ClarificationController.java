package com.cms.audit.api.Clarifications.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.multipart.MultipartFile;

import com.cms.audit.api.Clarifications.dto.ClarificationDTO;
import com.cms.audit.api.Clarifications.dto.EditClarificationDTO;
import com.cms.audit.api.Clarifications.dto.GenerateCKDTO;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.service.ClarificationService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.pdf.GeneratePdf;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_CLARIFICATION)
public class ClarificationController {

    @Autowired
    private ClarificationService service;

    @GetMapping
    public ResponseEntity<Object> get(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = service.getAll(page.orElse(0), size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        GlobalResponse response = service.getOneById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = service.getByDateRange(start_date, end_date, page.orElse(0),size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<InputStreamResource> getFileName(@PathVariable("fileName") String fileName)
            throws IOException {
        Clarification response = service.downloadFile(fileName);
        String path = response.getFile_path();
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource isr = new InputStreamResource(inputStream);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.valueOf("application/pdf"));
        httpHeaders.set("Content-Disposition", "inline; filename=" + response.getFileName());

        return new ResponseEntity<InputStreamResource>(isr, httpHeaders, HttpStatus.ACCEPTED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> save(@RequestBody EditClarificationDTO dto, @PathVariable("id") Long id) {
        GlobalResponse response = service.edit(dto,id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }
    
    @PostMapping("/generate")
    public ResponseEntity<Object> generateNumber(@RequestBody GenerateCKDTO dto) {
        GlobalResponse response = service.generateCK(dto);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PostMapping(value = "/file/{id}", consumes = { MediaType.APPLICATION_PDF_VALUE, "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" })
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file,
            @PathVariable("id") Long id) {
        GlobalResponse response = service.uploadFile(file, id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

}
