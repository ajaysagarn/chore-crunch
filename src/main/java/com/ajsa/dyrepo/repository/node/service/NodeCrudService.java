package com.ajsa.dyrepo.repository.node.service;

import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.property.model.Property;
import com.ajsa.dyrepo.util.RepositoryException;
import com.ajsa.dyrepo.repository.node.model.NodeResponse;

import java.util.ArrayList;
import java.util.Optional;

public interface NodeCrudService {

    public Node createNode(String parentNodeIdPath, String nodeId, ArrayList<Property> properties) throws RepositoryException;

    public Optional<NodeResponse> readNode(String nodeIdPath, Integer levels) throws RepositoryException;

    public Node updateNodeProperties(String nodeIdPath, ArrayList<Property> properties) throws RepositoryException;

    public Node updateNode(Node n) throws RepositoryException;

    public void deleteNode(String nodeId, Boolean deleteContent) throws RepositoryException;

    public Node createRepositoryNode();


}
