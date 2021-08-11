package com.ajsa.dyrepo.repository.property.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.*;

@Getter
@Setter
@Builder
@DynamoDBDocument
public class Property {
    private String id;
    private String name;
    private String type;
    private String value;

    public Property() {
    }

    public Property(String id, String name, String type, String value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
