package com.chore.crunch.repository.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.chore.crunch.repository.model.Node;
import com.chore.crunch.repository.service.NodeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController()
@RequestMapping("repo/api/v1")
public class NodeCRUDController {

    private NodeCrudService nodeCrudService;

    @Autowired
    public NodeCRUDController(NodeCrudService nodeCrudService){
        this.nodeCrudService = nodeCrudService;
    }

    @RequestMapping(value = "node", produces = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody Node node) {
        try {
            Node response = nodeCrudService.createNode(node);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }


    @RequestMapping(value = "node", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity readNode(@RequestBody String nodeId) {
        try {
            Node response = nodeCrudService.readNode(nodeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "node", produces = {"application/json"}, method = RequestMethod.PUT)
    public ResponseEntity updateNode(@RequestBody Node node) {
        try {
            Node response = nodeCrudService.updateNode(node);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @RequestMapping(value = "node", produces = {"application/json"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteNode(@RequestBody String nodeId) {
        try {
            nodeCrudService.deleteNode(nodeId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}
