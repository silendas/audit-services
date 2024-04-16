package com.cms.audit.api.NewsInspection.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.NewsInspection.service.NewsInspectionService;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_BAP)
public class NewsInspecionController {

        @Autowired
        private NewsInspectionService service;

        @GetMapping
        public ResponseEntity<Object> getAll(
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = service.getAll(page.orElse(0), size.orElse(10), start_date.orElse(null), end_date.orElse(null));
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> getOne(@PathVariable("id") Long id) {
                GlobalResponse response = service.getOneById(id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

          @GetMapping("/file/{fileName}")
    public ResponseEntity<InputStreamResource> getFileName(@PathVariable("fileName") String fileName)
            throws IOException {
        NewsInspection response = service.downloadFile(fileName);
        String path = response.getFile_path();
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource isr = new InputStreamResource(inputStream);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.valueOf("application/pdf"));
        httpHeaders.set("Content-Disposition", "inline; filename=" + response.getFileName());

        return new ResponseEntity<InputStreamResource>(isr, httpHeaders, HttpStatus.OK);
    }

        @PostMapping("/upload")
        public ResponseEntity<Object> upload(@RequestParam(value = "file", required = false) MultipartFile file,
                        @ModelAttribute("bap_id") Long id) {
                GlobalResponse response = service.uploadFile(file, id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

}
