package com.ajsa.dyrepo.repository.node.dao;

import com.ajsa.dyrepo.repository.node.model.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class NodeProperties {
    private ArrayList<Node.Property> properties;
}
