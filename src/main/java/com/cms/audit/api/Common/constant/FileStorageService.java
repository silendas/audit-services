package com.cms.audit.api.Common.constant;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(Environment env) {
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir-cl", "./uploaded/clarification"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    private String getFileExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    public String storeFile(MultipartFile file) {
        // Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or not provided.");
        }

        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileNameWithoutExtension = FilenameUtils.removeExtension(originalFileName);
        String fileExtension = getFileExtension(originalFileName);
        String uniqueFileName = fileNameWithoutExtension + "-" + new Date().getTime() + "." + fileExtension;

        try {
            // Check if the filename contains invalid characters
            if (uniqueFileName.contains("..")) {
                throw new RuntimeException(
                        "Sorry! Filename contains invalid path sequence " + uniqueFileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

            // Handle file name conflicts
            int count = 1;
            while (Files.exists(targetLocation)) {
                uniqueFileName = fileNameWithoutExtension + "-" + new Date().getTime() + "-" + count + "." + fileExtension;
                targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
                count++;
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + uniqueFileName + ". Please try again!", ex);
        }
    }
}
