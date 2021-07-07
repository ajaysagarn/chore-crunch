package com.chore.crunch.repository.service;

import com.chore.crunch.repository.dao.NodeDao;
import com.chore.crunch.repository.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeCrudServiceImpl implements NodeCrudService{

    private final NodeDao nodeDao;

    @Autowired
    public NodeCrudServiceImpl(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @Override
    public Node createNode(Node node) {
        return nodeDao.createNode(node);
    }

    @Override
    public Node readNode(String nodeId) {
        return nodeDao.readNode(nodeId);
    }

    @Override
    public Node updateNode(Node node) {
        return nodeDao.updateNode(node);
    }

    @Override
    public void deleteNode(String nodeId) {
        nodeDao.deleteNode(nodeId);
    }
}
