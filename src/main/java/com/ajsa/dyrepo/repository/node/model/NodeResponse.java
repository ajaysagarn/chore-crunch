package com.ajsa.dyrepo.repository.node.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class NodeResponse extends Node {
    ArrayList<Node> children;

    public NodeResponse(){
        super();
        this.children = new ArrayList<>();
    }

    public NodeResponse(ArrayList<Node> children) {
        super();
        this.children = children;
    }
}