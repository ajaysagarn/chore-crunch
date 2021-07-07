package com.chore.crunch.repository.service;

import com.chore.crunch.repository.model.Node;

public interface NodeCrudService {

    public Node createNode(Node node);

    public Node readNode(String nodeId);

    public Node updateNode(Node node);

    public void deleteNode(String nodeId);


}
