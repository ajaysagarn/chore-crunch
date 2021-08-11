package com.ajsa.dyrepo.repository.node.model;

import com.ajsa.dyrepo.repository.property.model.Property;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "This model represents a node stored in the repository")
@DynamoDBTable(tableName = "chore_crunch")
public class Node {

    public enum NodeType {
        REPO , FOLDER, DOCUMENT
    }

    @DynamoDBHashKey(attributeName = "nodeId")
    private String nodeId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "nodePath", attributeName = "path")
    private String path;

    @DynamoDBAttribute(attributeName = "name")
    private String name;

    @DynamoDBAttribute(attributeName = "nodeType")
    private String nodeType;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "parentNodeIndex", attributeName = "parentNodeId")
    private String parentNodeId;


    @DynamoDBAttribute(attributeName = "createdAt")
    private Long createdAt;

    @DynamoDBAttribute(attributeName = "modifiedAt")
    private Long modifiedAt;

    @DynamoDBAttribute(attributeName = "properties")
    private List<Property> properties;

    public Node(){
        this.createdAt = Instant.now().getEpochSecond();
        this.modifiedAt = Instant.now().getEpochSecond();
    }

    public Node(String nodeId, String path, String name, String nodeType, String parentNodeId, Long createdAt, Long modifiedAt, List<Property> properties) {
        this.nodeId = nodeId;
        this.path = path;
        this.name = name;
        this.nodeType = nodeType;
        this.parentNodeId = parentNodeId;
        this.createdAt = (createdAt == null)? this.createdAt = Instant.now().getEpochSecond(): createdAt;
        this.modifiedAt = Instant.now().getEpochSecond();
        this.properties = properties;
    }

}
