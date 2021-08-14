package com.ajsa.dyrepo.repository.content.service;

import com.ajsa.dyrepo.repository.content.model.Content;
import com.ajsa.dyrepo.repository.content.model.ReadContentResponse;
import com.ajsa.dyrepo.util.RepositoryException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContentService {
    public Content addContent(String nodeIdPath, String contentId, MultipartFile file) throws RepositoryException, IOException;
    public ReadContentResponse readContent(String nodeIdPath, String contentId) throws RepositoryException, IOException;
    public void deleteContent(String nodeIdPath, String contentId) throws RepositoryException;
    public void deleteAllContent(String nodeIdPath) throws RepositoryException;
    public void deleteNodeFolder(String nodePath) throws RepositoryException;
    public String getPresignedContentUrl(String nodeIdPath, String contentId) throws RepositoryException;
}
