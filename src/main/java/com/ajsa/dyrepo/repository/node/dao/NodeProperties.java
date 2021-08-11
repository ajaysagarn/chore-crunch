package com.ajsa.dyrepo.repository.node.dao;

import com.ajsa.dyrepo.repository.node.model.Node;
import com.ajsa.dyrepo.repository.property.model.Property;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class NodeProperties {
    private ArrayList<Property> properties;
}
