package com.ajsa.dyrepo.repository.property.controller;

import com.ajsa.dyrepo.repository.property.model.Property;
import com.ajsa.dyrepo.repository.property.service.PropertyService;
import com.ajsa.dyrepo.util.RepositoryException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class PropertyCRUDController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/property")
    public ResponseEntity readProperty(@RequestParam(required = false) String nodeId,
                                   @RequestParam(required = false) String nodePath,
                                   @RequestParam String propertyId ) {
        try {
            Optional<Property> node = propertyService.readProperty(nodeId != null?nodeId: nodePath, propertyId);
            return ResponseEntity.status(HttpStatus.CREATED).body(node.get());
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }

    @PostMapping("/property")
    public ResponseEntity createProperty(@RequestParam(required = false) String nodeId,
                                       @RequestParam(required = false) String nodePath,
                                       @RequestBody Property property) {
        try {
            Property createdProperty = propertyService.createProperty(nodeId != null?nodeId: nodePath, property);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }

    @RequestMapping(value = "property", produces = {"application/json"}, method = RequestMethod.PUT)
    public ResponseEntity updateProperty(
            @RequestParam(required = false) String nodeId,
            @RequestParam(required = false) String nodePath,
            @RequestBody Property property) {
        try {
            Property response = propertyService.updateProperty((nodeId == null)?nodePath:nodeId,property);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        } catch (AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (RepositoryException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }

    @RequestMapping(value = "property", produces = {"application/json"}, method = RequestMethod.DELETE)
    public ResponseEntity deleteProperty( @RequestParam(required = false) String nodeId,
                                      @RequestParam(required = false) String nodePath,
                                          @RequestParam String propertyId) {
        try {
            propertyService.deleteProperty((nodeId !=null)?nodeId:nodePath, propertyId);
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
