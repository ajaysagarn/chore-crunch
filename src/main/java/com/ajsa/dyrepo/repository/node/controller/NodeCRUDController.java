package com.ajsa.dyrepo.repository.node.controller;

import com.ajsa.dyrepo.util.RepositoryException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.ajsa.dyrepo.repository.node.dao.NodeProperties;
import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.node.model.NodeResponse;
import com.ajsa.dyrepo.repository.node.service.NodeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController()
@RequestMapping("api/v1")
public class NodeCRUDController {

    private NodeCrudService nodeCrudService;

    @Autowired
    public NodeCRUDController(NodeCrudService nodeCrudService){
        this.nodeCrudService = nodeCrudService;
    }

    @PostMapping(value = "node/{parentNodeId}/{nodeName}", produces = {"application/json"})
    public ResponseEntity createNode(
            @PathVariable String parentNodeId,
            @PathVariable String nodeName, @RequestBody NodeProperties nodeProperties) {
        try {
            Node response = nodeCrudService.createNode(parentNodeId, nodeName, nodeProperties.getProperties());
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getNodeId());
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e){
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }


    @GetMapping("/node")
    public ResponseEntity readNode(@RequestParam(required = false) String nodeId,
                                   @RequestParam(required = false) String nodePath,
                                   @RequestParam(required = false, defaultValue = "0") Integer levels ) {
        try {
            Optional<NodeResponse> node = nodeCrudService.readNode(nodeId != null?nodeId: nodePath, levels);
            return ResponseEntity.status(HttpStatus.CREATED).body(node.get());
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }

    @RequestMapping(value = "node", produces = {"application/json"}, method = RequestMethod.PUT)
    public ResponseEntity updateNode(
            @RequestParam(required = false) String nodeId,
            @RequestParam(required = false) String nodePath,
            @RequestBody NodeProperties properties) {
        try {
            Node response = nodeCrudService.updateNode((nodeId == null)?nodePath:nodeId,properties.getProperties());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }

    @RequestMapping(value = "node", produces = {"application/json"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteNode( @RequestParam(required = false) String nodeId,
                                      @RequestParam(required = false) String nodePath) {
        try {
            nodeCrudService.deleteNode((nodeId !=null)?nodeId:nodePath);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }

}
