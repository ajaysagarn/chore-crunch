package com.ajsa.dyrepo.repository.repo.service;

import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.node.service.NodeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoServiceImpl implements RepoService{

    @Autowired
    private NodeCrudService nodeCrudService;

    @Override
    public Node createRepo() {
        return nodeCrudService.createRepositoryNode();
    }
}
