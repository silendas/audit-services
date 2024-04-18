package com.cms.audit.api.Report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Report.service.ReportService;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_REPORT)
public class ReportController {

        @Autowired
        private ReportService service;

        @GetMapping("/clarification")
        public ResponseEntity<InputStreamResource> downloadCL(
                        @RequestParam("name") Optional<String> name,
                        @RequestParam(required = false) Optional<Long> area_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date)
                        throws IOException {
                String fileName;
                if (start_date != null && end_date != null) {
                        fileName = convertDateToRoman.convertDateHehe(start_date) + "-" + convertDateToRoman.convertDateHehe(end_date) + "-report.xlsx";
                } else {
                        fileName = "all-report.xlsx";
                }
                ByteArrayInputStream inputStream = service.getDataDownloadClarification(name.orElse(null),
                                area_id.orElse(null), start_date,
                                end_date);
                InputStreamResource resource = new InputStreamResource(inputStream);

                ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);
                return response;
        }

        @GetMapping("/lha")
        public ResponseEntity<InputStreamResource> downloadLHA(
                        @RequestParam(required = false) Optional<Long> user_id,
                        @RequestParam(required = false) Optional<Long> area_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date)
                        throws IOException {
                return service.getDataDownloadLHA(user_id.orElse(null), area_id.orElse(null), start_date, end_date);
        }

}
