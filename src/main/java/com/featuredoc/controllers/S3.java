package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class S3 {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/feature/{featureID}/version/{featureVersionID}/document")
    public ResponseEntity<Map<String, String>> uploadFile(@PathVariable("featureID") long featureID, @PathVariable("featureVersionID") long featureVersionID, @RequestParam("file") MultipartFile file) {
        try {
            String objectKey = createFilePath(file.getOriginalFilename(), featureID, featureVersionID);
            String objectUrl = s3Service.uploadFile(objectKey, file);
            return ResponseEntity.of(Optional.of(Map.of(
                    "url", objectUrl
            )));
        } catch (Exception e) {
            return ResponseEntity.of(Optional.of(Map.of(
                    "error", e.getMessage()
            )));
        }
    }

    private String createFilePath(String originalFilename, long featureID, long featureVersionID) {
        String[] fileNameParts = originalFilename.split("\\.");
        String extension = "";
        if (fileNameParts.length > 0) {
            extension = fileNameParts[fileNameParts.length - 1];
            return featureID + "/" + featureVersionID + "/document." + extension;
        } else {
            return featureID + "/" + featureVersionID + "/document";
        }
    }

}