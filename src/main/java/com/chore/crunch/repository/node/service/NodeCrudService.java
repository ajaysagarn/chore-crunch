package com.chore.crunch.repository.node.service;

import com.chore.crunch.repository.node.model.Node;
import com.chore.crunch.repository.node.model.NodeResponse;
import com.chore.crunch.util.RepositoryException;

import java.util.ArrayList;
import java.util.Optional;

public interface NodeCrudService {

    public Node createNode(String parentNodeId, String nodeId, ArrayList<Node.Property> properties) throws RepositoryException;

    public Optional<NodeResponse> readNode(String nodeId, Integer levels) throws RepositoryException;

    public Node updateNode(String nodeId, ArrayList<Node.Property> properties) throws RepositoryException;

    public void deleteNode(String nodeId) throws RepositoryException;

    public Node createRepositoryNode();


}
