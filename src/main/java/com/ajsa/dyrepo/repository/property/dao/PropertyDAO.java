package com.ajsa.dyrepo.repository.property.dao;

import com.ajsa.dyrepo.repository.node.model.Node.Property;
import com.ajsa.dyrepo.util.RepositoryException;

import java.util.Optional;

public interface PropertyDAO {

    public Property createProperty(String nodeIdPath, Property node);

    public Optional<Property> readProperty(String nodeIdPath, String propertyId) throws RepositoryException;

    public Property updateProperty(String nodeIdPath, Property property);

    public void deleteProperty(String propertyPath, String propertyId);

}