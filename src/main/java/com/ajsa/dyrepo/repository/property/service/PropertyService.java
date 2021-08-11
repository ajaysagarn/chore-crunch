package com.ajsa.dyrepo.repository.property.service;

import com.ajsa.dyrepo.repository.property.model.Property;
import com.ajsa.dyrepo.util.RepositoryException;

import java.util.Optional;

public interface PropertyService {

    public Property createProperty(String nodeIdPath, Property node) throws RepositoryException;

    public Optional<Property> readProperty(String nodeIdPath, String propertyId) throws RepositoryException;

    public Property updateProperty(String nodeIdPath, Property property) throws RepositoryException;

    public void deleteProperty(String propertyPath, String propertyId) throws RepositoryException;

}
