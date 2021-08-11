package com.ajsa.dyrepo.repository.property.service;

import com.ajsa.dyrepo.repository.node.dao.NodeDao;
import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.node.model.NodeResponse;
import com.ajsa.dyrepo.repository.property.model.Property;
import com.ajsa.dyrepo.util.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private NodeDao nodeDao;

    @Override
    public Property createProperty(String nodeIdPath, Property property) throws RepositoryException {
            Optional<NodeResponse> node = nodeDao.readNode(nodeIdPath, 0);
            if(node.isEmpty()){
                throw new RepositoryException(
                        HttpStatus.NOT_FOUND.value(),
                        "Parent node with node Id/path ".concat(nodeIdPath).concat(" not found.")
                );
            }

            Node currNode = node.get();
            List<Property> properties = currNode.getProperties();

            boolean isExists = properties.stream().anyMatch(property1 -> property1.getId() == property.getId());
            if(isExists){
                throw new RepositoryException(
                        HttpStatus.CONFLICT.value(),
                        "Property with id ".concat(property.getId()).concat(" already exists in the Node.")
                );
            }

            properties.add(property);
            currNode.setProperties(properties);
            nodeDao.updateNode(currNode);
        return property;
    }

    @Override
    public Optional<Property> readProperty(String nodeIdPath, String propertyId) throws RepositoryException {
        Optional<NodeResponse> node = nodeDao.readNode(nodeIdPath, 0);
        if(node.isEmpty()){
            throw new RepositoryException(
                    HttpStatus.NOT_FOUND.value(),
                    "Parent node with node Id/path ".concat(nodeIdPath).concat(" not found.")
            );
        }
        return getPropertyWithId(node.get(), propertyId);
    }

    @Override
    public Property updateProperty(String nodeIdPath, Property property) throws RepositoryException {

        Optional<NodeResponse> node = nodeDao.readNode(nodeIdPath, 0);
        if(node.isEmpty()){
            throw new RepositoryException(
                    HttpStatus.NOT_FOUND.value(),
                    "Parent node with node Id/path ".concat(nodeIdPath).concat(" not found.")
            );
        }

        List<Property> properties = node.get().getProperties();

        properties.remove(getPropertyWithId(node.get(),property.getId()).get());
        properties.add(property);
        node.get().setProperties(properties);
        nodeDao.updateNode(node.get());

        return property;
    }

    @Override
    public void deleteProperty(String nodeIdPath, String propertyId) throws RepositoryException {
        Optional<NodeResponse> node = nodeDao.readNode(nodeIdPath, 0);
        if(node.isEmpty()){
            throw new RepositoryException(
                    HttpStatus.NOT_FOUND.value(),
                    "Node with node Id/path ".concat(nodeIdPath).concat(" not found.")
            );
        }

        Node currNode = node.get();
        List<Property> properties = currNode.getProperties();
        Optional<Property> prop = getPropertyWithId(currNode, propertyId);
        properties.remove(prop.get());

        currNode.setProperties(properties);

        nodeDao.updateNode(currNode);
    }


    public Optional<Property> getPropertyWithId(Node node, String propertyId) throws RepositoryException {

        List<Property> properties = node.getProperties();
        Optional<Property> pr = properties.stream().filter(p -> (p.getId().equals(propertyId))).findFirst();

        if(pr.isEmpty()){
            throw new RepositoryException(
                    HttpStatus.NOT_FOUND.value(),
                    "Property with id ".concat(propertyId).concat(" not found.")
            );
        }

        return pr;
    }


}
