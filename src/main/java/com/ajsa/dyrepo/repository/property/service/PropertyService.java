package com.ajsa.dyrepo.repository.property.service;

import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.util.RepositoryException;

import java.util.Optional;

public interface PropertyService {

    public Node.Property createProperty(String nodeIdPath, Node.Property node) throws RepositoryException;

    public Optional<Node.Property> readProperty(String nodeIdPath, String propertyId) throws RepositoryException;

    public Node.Property updateProperty(String nodeIdPath, Node.Property property) throws RepositoryException;

    public void deleteProperty(String propertyPath, String propertyId) throws RepositoryException;

}
