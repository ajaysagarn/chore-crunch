package com.ajsa.dyrepo.repository.repo.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.repo.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("repo/api/v1")
public class RepoController {

    @Autowired
    private RepoService repoService;


    @PostMapping("repo/createnewrepo")
    public ResponseEntity createRepo(){
        try {
            Node response = repoService.createRepo();
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getNodeId());
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }



}
