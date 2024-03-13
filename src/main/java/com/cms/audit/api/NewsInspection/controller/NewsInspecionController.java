package com.cms.audit.api.NewsInspection.controller;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cms.audit.api.NewsInspection.service.NewsInspectionService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_BAP)
public class NewsInspecionController {

        @Autowired
        private NewsInspectionService service;

        @GetMapping
        public ResponseEntity<Object> getAll(
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = service.getAll(page.orElse(0), size.orElse(10));
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> getAll(@PathVariable("id") Long id) {
                GlobalResponse response = service.getOneById(id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @PostMapping("/pdf")
        public ResponseEntity<Object> generate() throws FileNotFoundException, MalformedURLException {
                service.generatePdf();
                return ResponseEntittyHandler.allHandler(null, "OK",
                HttpStatus.OK,
                null);
        }

        @GetMapping("/filter")
        public ResponseEntity<Object> getByDateRange(
                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date,
                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = service.getByDateRange(start_date, end_date, page.orElse(0), size.orElse(10));
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @PostMapping(value = "/file/{id}", consumes = { MediaType.APPLICATION_PDF_VALUE, "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" })
        public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file,
                        @PathVariable("id") Long id) {
                GlobalResponse response = service.uploadFile(file, id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

}
