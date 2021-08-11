package com.ajsa.dyrepo.repository.property.dao;

import com.ajsa.dyrepo.repository.node.dao.NodeDao;
import com.ajsa.dyrepo.repository.property.model.Property;
import com.ajsa.dyrepo.util.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PropertyDaoImpl implements PropertyDAO {

    @Autowired
    private NodeDao nodeDao;

    @Override
    public Property createProperty(String nodeIdPath, Property property) {
        return null;
    }

    @Override
    public Optional<Property> readProperty(String nodeIdPath, String propertyId) throws RepositoryException {
        return Optional.empty();
    }

    @Override
    public Property updateProperty(String nodeIdPath, Property property) {
        return null;
    }

    @Override
    public void deleteProperty(String propertyPath, String propertyId) {

    }
}
