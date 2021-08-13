package com.ajsa.dyrepo.repository.content.controller;

import com.ajsa.dyrepo.repository.content.model.Content;
import com.ajsa.dyrepo.repository.content.model.ReadContentResponse;
import com.ajsa.dyrepo.repository.content.service.ContentService;
import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.util.RepositoryException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.internal.Mimetypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController()
@RequestMapping("api/v1")
public class ContentCRUDController {

    @Autowired
    private ContentService contentService;

    @PostMapping(value = "content", produces = {"application/json"}, consumes = "multipart/form-data")
    public ResponseEntity uploadContent(@RequestParam String nodeIdPath, @RequestParam(required = false) String contentId ,@RequestParam("file") MultipartFile file){
        try {
            Content content = contentService.addContent(nodeIdPath,contentId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(content);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }catch (AmazonClientException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

   @GetMapping(value = "content", produces = Mimetypes.MIMETYPE_OCTET_STREAM)
   public ResponseEntity getContent(@RequestParam String nodeIdPath, @RequestParam(required = false) String contentId){
       try {
           ReadContentResponse content = contentService.readContent(nodeIdPath,contentId);
           return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + content.getFileName() + "\"").body(content.getContent());
       } catch (AmazonServiceException e) {
           throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
       }catch (RepositoryException e) {
           return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
       } catch (AmazonClientException | IOException e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
       }
   }

    @DeleteMapping(value = "content", produces = {"application/json"})
    public ResponseEntity deleteContent(@RequestParam String nodeIdPath, @RequestParam(required = false) String contentId){
        try {
            contentService.deleteContent(nodeIdPath,contentId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Content deleted.");
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
