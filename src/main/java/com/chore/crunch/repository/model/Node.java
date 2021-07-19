package com.chore.crunch.repository.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This model represents a node stored in the repository")
@DynamoDBTable(tableName = "chore_crunch")
public class Node {

    @Schema(description = "nodeId", example = "Hvk031weL")
    @DynamoDBHashKey(attributeName = "nodeId")
    @DynamoDBAutoGeneratedKey
    private String nodeId;
    @DynamoDBAttribute(attributeName = "name")
    private String name;
    @DynamoDBAttribute(attributeName = "path")
    @DynamoDBRangeKey
    private String path;
    @DynamoDBAttribute(attributeName = "properties")
    @DynamoDBTypeConvertedJson
    private List<Property> properties;


    @Getter
    @Setter
    @Builder
    @DynamoDBDocument
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {
        private String id;
        private String name;
        private String type;
        private String value;
    }

}
