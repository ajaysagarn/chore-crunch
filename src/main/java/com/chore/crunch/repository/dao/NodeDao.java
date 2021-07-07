package com.chore.crunch.repository.dao;

import com.chore.crunch.repository.model.Node;

public interface NodeDao {

    public Node createNode(Node node);

    public Node readNode(String nodeId);

    public Node updateNode(Node node);

    public void deleteNode(String nodeId);

}
