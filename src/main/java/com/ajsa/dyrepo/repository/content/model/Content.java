package com.ajsa.dyrepo.repository.content.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.*;

@Getter
@Setter
@Builder
@DynamoDBDocument
public class Content {

    private String id;
    private String path;
    private long length;
    private String eTag;
    private String fileName;

    public Content() {
    }

    public Content(String id, String path, long length, String eTag, String fileName) {
        this.id = id;
        this.path = path;
        this.length = length;
        this.eTag = eTag;
        this.fileName = fileName;
    }
}
