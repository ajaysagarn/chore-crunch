package com.ajsa.dyrepo.repository.repo.service;

import com.ajsa.dyrepo.repository.node.model.Node;

public interface RepoService {

    /**
     * Create a Root repository node. This will be the parent node for all other nodes
     * in the repository
     * @return
     */
    public Node createRepo();

}
