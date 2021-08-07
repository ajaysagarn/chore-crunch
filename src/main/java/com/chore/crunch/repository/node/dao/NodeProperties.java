package com.chore.crunch.repository.node.dao;

import com.chore.crunch.repository.node.model.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class NodeProperties {
    private ArrayList<Node.Property> properties;
}
