package com.ajsa.dyrepo.repository.content.dao;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

public interface ContentDao {

    PutObjectResponse addContent(String contentPath, MultipartFile file) throws IOException;
    Resource readContent(String contentPath, String filename) throws IOException;
    void deleteContent(String contentPath);
    void deleteFolder(String folderPath);

    String getPresignedUrl(String contentPath);
    String getFolderPresignedUrls(String folderPath);


}
