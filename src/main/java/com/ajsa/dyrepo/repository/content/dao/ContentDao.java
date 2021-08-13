package com.ajsa.dyrepo.repository.content.dao;

import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContentDao {

    public PutObjectResult addContent(String contentPath, MultipartFile file) throws IOException;
    public Resource readContent(String contentPath, String filename) throws IOException;
    public void deleteContent(String contentPath);

    void deleteFolder(String folderPath);
}
