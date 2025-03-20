package com.featuredoc.services;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.featuredoc.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.bucketUrl}")
    private String bucketUrl;

    public String uploadFile(String key, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), null);
        PutObjectResult result = amazonS3.putObject(putObjectRequest);
        amazonS3.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
        return bucketUrl + key;
    }

    public Optional<String> getFileUrl(String folderPath) {
        ObjectListing listing = amazonS3.listObjects(bucketName, folderPath);
        if (listing.getObjectSummaries().size() > 0) {
            System.out.println(folderPath);
            return Optional.of(bucketUrl + listing.getObjectSummaries().get(0).getKey());
        } else {
            return Optional.empty();
        }
    }
}