package com.chore.crunch.repository.node.dao;

import com.chore.crunch.repository.node.model.Node;
import com.chore.crunch.repository.node.model.NodeResponse;
import com.chore.crunch.util.RepositoryException;

import java.util.Optional;

public interface NodeDao {

    public Node createNode(Node node);

    public Optional<NodeResponse> readNode(String nodeId, Integer levels) throws RepositoryException;

    public Node updateNode(Node node);

    public Node createRepo();

    public void deleteNode(String nodeId);

}
