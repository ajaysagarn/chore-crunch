package com.ajsa.dyrepo.repository.node.dao;

import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.node.model.NodeResponse;
import com.ajsa.dyrepo.util.RepositoryException;

import java.util.Optional;

public interface NodeDao {

    public Node createNode(Node node);

    public Optional<NodeResponse> readNode(String nodeId, Integer levels) throws RepositoryException;

    public Node updateNode(Node node);

    public Node createRepo();

    public void deleteNode(String nodeId);

}
